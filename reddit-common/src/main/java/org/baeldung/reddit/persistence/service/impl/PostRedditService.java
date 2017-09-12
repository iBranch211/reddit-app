package org.baeldung.reddit.persistence.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.baeldung.persistence.dao.PostRepository;
import org.baeldung.persistence.dao.SubmissionResponseRepository;
import org.baeldung.persistence.model.Post;
import org.baeldung.persistence.model.SubmissionResponse;
import org.baeldung.persistence.model.User;
import org.baeldung.reddit.persistence.service.IPostRedditService;
import org.baeldung.reddit.util.OnPostSubmittedEvent;
import org.baeldung.reddit.util.PostScores;
import org.baeldung.reddit.util.RedditApiConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class PostRedditService implements IPostRedditService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final static String SCORE_TEMPLATE = "score  %d %s minimum score %d";
    private final static String TOTAL_VOTES_TEMPLATE = "total votes  %d %s minimum total votes %d";

    @Autowired
    @Qualifier("schedulerRedditTemplate")
    private OAuth2RestTemplate redditRestTemplate;

    @Autowired
    @Qualifier("simpleRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private PostRepository postReopsitory;

    @Autowired
    private SubmissionResponseRepository submissionResponseReopsitory;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    // API

    @Override
    public void submitPost(final Post post) {
        try {
            submitPostInternal(post);
        } catch (final Exception e) {
            logger.error("Error occurred while submitting post " + post.toString(), e);
        }
    }

    @Override
    public PostScores getPostScores(final Post post) {
        JsonNode node = restTemplate.getForObject("http://www.reddit.com/r/" + post.getSubreddit() + "/comments/" + post.getRedditID() + ".json", JsonNode.class);

        final PostScores postScore = new PostScores();
        node = node.get(0).get("data").get("children").get(0).get("data");
        postScore.setScore(node.get("score").asInt());

        final float ratio = node.get("upvote_ratio").floatValue();
        postScore.setTotalVotes(Math.round(postScore.getScore() / ((2 * ratio) - 1)));
        postScore.setNoOfComments(node.get("num_comments").asInt());

        logger.info(postScore.toString());

        return postScore;
    }

    @Override
    public void deletePost(final String redditId) {
        logger.info("Deleting post with id = {}", redditId);
        //
        final MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
        param.add("id", "t3_" + redditId);
        final JsonNode node = redditRestTemplate.postForObject("https://oauth.reddit.com/api/del.json", param, JsonNode.class);

        logger.info("Deleting post response = {}", node.toString());
    }

    @Override
    public void checkAndReSubmit(final Post post) {
        try {
            checkAndReSubmitInternal(post);
        } catch (final Exception e) {
            logger.error("Error occurred while checking and resubmitting post = " + post.toString(), e);
        }
    }

    @Override
    public void checkAndDelete(final Post post) {
        try {
            checkAndDeleteInternal(post);
        } catch (final Exception e) {
            logger.error("Error occurred while checking and deleting post " + post.toString(), e);
        }
    }

    // === private methods

    private void submitPostInternal(final Post post) {
        oauth2Authenticate(post.getUser());
        //
        final MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
        param.add(RedditApiConstants.TITLE, post.getTitle());
        param.add(RedditApiConstants.SR, post.getSubreddit());
        param.add(RedditApiConstants.URL, post.getUrl());
        param.add(RedditApiConstants.API_TYPE, "json");
        param.add(RedditApiConstants.KIND, "link");
        param.add(RedditApiConstants.RESUBMIT, "true");
        param.add(RedditApiConstants.THEN, "comments");
        if (post.isSendReplies()) {
            param.add(RedditApiConstants.SENDREPLIES, "true");
        }

        logger.info("Submitting link with these parameters = {}", param.entrySet());
        final JsonNode node = redditRestTemplate.postForObject("https://oauth.reddit.com/api/submit", param, JsonNode.class);
        updatePostFromResponse(node, post);
    }

    private void updatePostFromResponse(final JsonNode node, final Post post) {
        final JsonNode errorNode = node.get("json").get("errors").get(0);
        if (errorNode == null) {
            post.setSent(true);
            post.setSubmissionsResponse(addAttemptResponse(post, "Submitted to Reddit"));
            post.setRedditID(node.get("json").get("data").get("id").asText());
            if (post.getNoOfAttempts() > 0) {
                post.setNoOfAttempts(post.getNoOfAttempts() - 1);
            }
            postReopsitory.save(post);

            logger.info("Successfully sent post = " + post.toString());

            final String email = post.getUser().getPreference().getEmail();
            logger.info("Sending notification email to " + email);
            eventPublisher.publishEvent(new OnPostSubmittedEvent(post, email));
        } else {
            post.setSubmissionsResponse(addAttemptResponse(post, errorNode.toString()));
            postReopsitory.save(post);
            logger.error("Error occurred: " + errorNode.toString() + "while submitting post " + post.toString());
        }
    }

    private void checkAndReSubmitInternal(final Post post) {
        if (didIntervalPass(post.getSubmissionDate(), post.getCheckAfterInterval())) {
            logger.info("Checking and Resubmitting post = {}", post.toString());
            final PostScores postScores = getPostScores(post);
            if (didPostGoalFail(post, postScores)) {
                oauth2Authenticate(post.getUser());
                deletePost(post.getRedditID());
                resetPost(post, getFailReason(post, postScores));
            } else {
                post.setNoOfAttempts(0);
                post.setRedditID(null);
                post.setSubmissionsResponse(updateLastAttemptResponse(post, "Post reached target score successfully " + getSuccessReason(post, postScores)));
                postReopsitory.save(post);
            }
        }
    }

    private void checkAndDeleteInternal(final Post post) {
        if (didIntervalPass(post.getSubmissionDate(), post.getCheckAfterInterval())) {
            logger.info("Checking and deleting post = {}", post.toString());
            final PostScores postScores = getPostScores(post);
            if (didPostGoalFail(post, postScores)) {
                oauth2Authenticate(post.getUser());
                deletePost(post.getRedditID());
                post.setSubmissionsResponse(updateLastAttemptResponse(post, "Deleted from reddit, consumed all attempts without reaching score " + getFailReason(post, postScores)));
                post.setRedditID(null);
                postReopsitory.save(post);
            } else {
                post.setNoOfAttempts(0);
                post.setRedditID(null);
                post.setSubmissionsResponse(updateLastAttemptResponse(post, "Post reached target score successfully " + getSuccessReason(post, postScores)));
                postReopsitory.save(post);
            }
        }
    }

    private boolean didIntervalPass(final Date submissonDate, final int postInterval) {
        final long currentTime = new Date().getTime();
        final long interval = currentTime - submissonDate.getTime();
        final long intervalInMinutes = TimeUnit.MINUTES.convert(interval, TimeUnit.MILLISECONDS);
        return intervalInMinutes > postInterval;
    }

    private void resetPost(final Post post, final String failReason) {
        long time = new Date().getTime();
        time += TimeUnit.MILLISECONDS.convert(post.getSubmitAfterInterval(), TimeUnit.MINUTES);
        post.setRedditID(null);
        post.setSubmissionDate(new Date(time));
        post.setSent(false);
        post.setSubmissionsResponse(updateLastAttemptResponse(post, "Deleted from Reddit, to be resubmitted " + failReason));

        postReopsitory.save(post);
    }

    protected boolean didPostGoalFail(final Post post, final PostScores postScores) {
        final boolean failToReachRequiredScore = postScores.getScore() < post.getMinScoreRequired();
        final boolean enoughTotalVotes = (postScores.getTotalVotes() >= post.getMinTotalVotes()) && (post.getMinTotalVotes() > 0);
        final boolean keepBecauseOfComments = (postScores.getNoOfComments() > 0) && post.isKeepIfHasComments();
        return (failToReachRequiredScore && !(keepBecauseOfComments || enoughTotalVotes));
    }

    protected String getFailReason(final Post post, final PostScores postScores) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Failed because ");
        builder.append(String.format(SCORE_TEMPLATE, postScores.getScore(), "<", post.getMinScoreRequired()));
        if (post.getMinTotalVotes() > 0) {
            builder.append(" and ");
            builder.append(String.format(TOTAL_VOTES_TEMPLATE, postScores.getTotalVotes(), "<", post.getMinTotalVotes()));
        }
        if (post.isKeepIfHasComments()) {
            builder.append(" and has no comments");
        }
        return builder.toString();
    }

    protected String getSuccessReason(final Post post, final PostScores postScores) {
        final StringBuilder builder = new StringBuilder();
        if (postScores.getScore() >= post.getMinScoreRequired()) {
            builder.append("Succeed because ");
            builder.append(String.format(SCORE_TEMPLATE, postScores.getScore(), ">=", post.getMinScoreRequired()));
            return builder.toString();
        }
        if ((post.getMinTotalVotes() > 0) && (postScores.getTotalVotes() >= post.getMinTotalVotes())) {
            builder.append("Succeed because ");
            builder.append(String.format(TOTAL_VOTES_TEMPLATE, postScores.getTotalVotes(), ">=", post.getMinTotalVotes()));
            return builder.toString();
        }
        return "Succeed because has comments";
    }

    private List<SubmissionResponse> addAttemptResponse(final Post post, final String response) {
        final List<SubmissionResponse> fullResponse = post.getSubmissionsResponse();
        final SubmissionResponse newResponse = new SubmissionResponse(fullResponse.size() + 1, response, post);
        submissionResponseReopsitory.save(newResponse);
        fullResponse.add(newResponse);
        return fullResponse;
    }

    private List<SubmissionResponse> updateLastAttemptResponse(final Post post, final String response) {
        final List<SubmissionResponse> fullResponse = post.getSubmissionsResponse();
        final int attemptNo = post.getSubmissionsResponse().size();
        SubmissionResponse oldResponse = null;
        for (int i = attemptNo - 1; i > -1; i--) {
            if (fullResponse.get(i).getAttemptNumber() == attemptNo) {
                oldResponse = fullResponse.get(i);
                fullResponse.remove(i);
                break;
            }
        }
        oldResponse.setContent(response);
        oldResponse.setScoreCheckDate(new Date());
        oldResponse = submissionResponseReopsitory.save(oldResponse);
        fullResponse.add(oldResponse);
        return fullResponse;
    }

    private void oauth2Authenticate(final User user) {
        final DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(user.getAccessToken());
        token.setRefreshToken(new DefaultOAuth2RefreshToken((user.getRefreshToken())));
        token.setExpiration(user.getTokenExpiration());
        redditRestTemplate.getOAuth2ClientContext().setAccessToken(token);
        //
        final UsernamePasswordAuthenticationToken userAuthToken = new UsernamePasswordAuthenticationToken(user.getUsername(), token.getValue(), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(userAuthToken);
        System.out.println(userAuthToken.isAuthenticated());
    }
}
