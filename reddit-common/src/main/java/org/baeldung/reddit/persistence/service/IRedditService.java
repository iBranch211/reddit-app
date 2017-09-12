package org.baeldung.reddit.persistence.service;

import java.util.List;

import org.baeldung.reddit.util.PostDto;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public interface IRedditService {

    List<String> submitPost(final PostDto postDto);

    List<String> searchSubreddit(final String query);

    void connectReddit(final boolean needsCaptcha, final OAuth2AccessToken token);

    String checkIfAlreadySubmittedfinal(final String url, final String subreddit);

    boolean isCurrentUserAccessTokenValid();
}
