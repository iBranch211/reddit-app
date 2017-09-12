package org.baeldung.reddit.persistence.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.User;
import org.baeldung.reddit.persistence.beans.RedditTemplate;
import org.baeldung.reddit.persistence.service.IRedditService;
import org.baeldung.reddit.util.PostDto;
import org.baeldung.reddit.util.RedditApiConstants;
import org.baeldung.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.JsonNode;

@Service
class RedditService implements IRedditService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedditTemplate redditTemplate;

    @Autowired
    private UserRepository userRepository;

    private List<String> subreddits;

    public RedditService() {
        loadSubreddits();
    }

    // API

    @Override
    public void connectReddit(final boolean needsCaptcha, final OAuth2AccessToken token) {
        final UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final User currentUser = userPrincipal.getUser();
        currentUser.setNeedCaptcha(needsCaptcha);
        currentUser.setAccessToken(token.getValue());
        currentUser.setRefreshToken(token.getRefreshToken().getValue());
        currentUser.setTokenExpiration(token.getExpiration());
        userRepository.save(currentUser);
    }

    @Override
    public List<String> submitPost(final PostDto postDto) {
        final MultiValueMap<String, String> param1 = constructParams(postDto);
        final JsonNode node = redditTemplate.submitPost(param1);
        return parseResponse(node);
    }

    @Override
    public List<String> searchSubreddit(final String query) {
        return subreddits.stream().filter(sr -> sr.startsWith(query)).limit(9).collect(Collectors.toList());
    }

    @Override
    public String checkIfAlreadySubmittedfinal(final String url, final String subreddit) {
        final JsonNode node = redditTemplate.searchForLink(url, subreddit);
        return node.get("data").get("children").toString();
    }

    @Override
    public boolean isCurrentUserAccessTokenValid() {
        final UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final User currentUser = userPrincipal.getUser();
        if (currentUser.getAccessToken() == null) {
            return false;
        }
        try {
            redditTemplate.needsCaptcha();
        } catch (final Exception e) {
            redditTemplate.setAccessToken(null);
            currentUser.setAccessToken(null);
            currentUser.setRefreshToken(null);
            currentUser.setTokenExpiration(null);
            userRepository.save(currentUser);
            return false;
        }
        return true;
    }

    // === private

    private final MultiValueMap<String, String> constructParams(final PostDto postDto) {
        final MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
        param.add(RedditApiConstants.TITLE, postDto.getTitle());
        param.add(RedditApiConstants.SR, postDto.getSubreddit());
        param.add(RedditApiConstants.URL, postDto.getUrl());
        param.add(RedditApiConstants.IDEN, postDto.getIden());
        param.add(RedditApiConstants.CAPTCHA, postDto.getCaptcha());
        param.add(RedditApiConstants.URL, postDto.getUrl());
        if (postDto.isSendreplies()) {
            param.add(RedditApiConstants.SENDREPLIES, "true");
        }

        param.add(RedditApiConstants.API_TYPE, "json");
        param.add(RedditApiConstants.KIND, "link");
        param.add(RedditApiConstants.RESUBMIT, "true");
        param.add(RedditApiConstants.THEN, "comments");
        return param;
    }

    private final List<String> parseResponse(final JsonNode node) {
        String result = "";
        final JsonNode errorNode = node.get("json").get("errors").get(0);
        if (errorNode != null) {
            for (final JsonNode child : errorNode) {
                result = result + child.toString().replaceAll("\"|null", "") + "<br>";
            }
            return Arrays.asList(result);
        } else {
            if ((node.get("json").get("data") != null) && (node.get("json").get("data").get("url") != null)) {
                return Arrays.asList("Post submitted successfully", node.get("json").get("data").get("url").asText());
            } else {
                return Arrays.asList("Error Occurred while parsing Response");
            }
        }
    }

    private void loadSubreddits() {
        subreddits = new ArrayList<String>();
        try {
            final Resource resource = new ClassPathResource("subreddits.csv");
            final Scanner scanner = new Scanner(resource.getFile());
            scanner.useDelimiter(",");
            while (scanner.hasNext()) {
                subreddits.add(scanner.next());
            }
            scanner.close();
        } catch (final IOException e) {
            logger.error("error while loading subreddits", e);
        }

    }

    // old autocomplete subreddit
    // private JsonNode searchSubredditNames(String query) {
    // final MultiValueMap<String, String> param = new
    // LinkedMultiValueMap<String, String>();
    // param.add("query", query);
    // final JsonNode node = redditTemplate.subredditNameSearch(query);
    // return node.get("names");
    // }
}
