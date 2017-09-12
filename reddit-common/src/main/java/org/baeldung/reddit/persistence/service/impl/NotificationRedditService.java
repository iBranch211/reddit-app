package org.baeldung.reddit.persistence.service.impl;

import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.Preference;
import org.baeldung.persistence.model.User;
import org.baeldung.reddit.persistence.service.INotificationRedditService;
import org.baeldung.reddit.util.OnNewPostReplyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class NotificationRedditService implements INotificationRedditService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final static String NOTIFICATION_TEMPLATE = "You have %d unread post replies.";
    private final static String MESSAGE_TEMPLATE = "%s replied on your post %s : %s";

    @Autowired
    @Qualifier("schedulerRedditTemplate")
    private OAuth2RestTemplate redditRestTemplate;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private UserRepository userRepository;

    // API

    @Override
    public void checkAndNotify(final Preference preference) {
        try {
            checkAndNotifyInternal(preference);
        } catch (final Exception e) {
            logger.error("Error occurred while checking and notifying = " + preference.getEmail(), e);
        }
    }

    // === private methods

    private void checkAndNotifyInternal(final Preference preference) {
        logger.info("Checking and Notifying", preference.getEmail());

        final User user = userRepository.findByPreference(preference);
        if ((user == null) || (user.getAccessToken() == null)) {
            return;
        }

        final DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(user.getAccessToken());
        token.setRefreshToken(new DefaultOAuth2RefreshToken((user.getRefreshToken())));
        token.setExpiration(user.getTokenExpiration());
        redditRestTemplate.getOAuth2ClientContext().setAccessToken(token);

        final JsonNode node = redditRestTemplate.getForObject("https://oauth.reddit.com/message/selfreply?mark=false", JsonNode.class);
        parseRepliesNode(preference.getEmail(), node);
    }

    private void parseRepliesNode(final String email, final JsonNode node) {
        final JsonNode allReplies = node.get("data").get("children");
        int unread = 0;
        for (final JsonNode msg : allReplies) {
            if (msg.get("data").get("new").asBoolean()) {
                unread++;
            }
        }
        if (unread == 0) {
            return;
        }

        final JsonNode firstMsg = allReplies.get(0).get("data");
        final String author = firstMsg.get("author").asText();
        final String postTitle = firstMsg.get("link_title").asText();
        final String content = firstMsg.get("body").asText();

        final StringBuilder builder = new StringBuilder();
        builder.append(String.format(NOTIFICATION_TEMPLATE, unread));
        builder.append("\n");
        builder.append(String.format(MESSAGE_TEMPLATE, author, postTitle, content));
        builder.append("\n");
        builder.append("Check all new replies at ");
        builder.append("https://www.reddit.com/message/unread/");

        eventPublisher.publishEvent(new OnNewPostReplyEvent(email, builder.toString()));
    }

}
