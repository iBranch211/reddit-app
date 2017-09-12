package org.baeldung.reddit.persistence.beans;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.util.concurrent.RateLimiter;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RedditTemplate {

    @Autowired
    @Qualifier("redditRestTemplate")
    private OAuth2RestTemplate redditRestTemplate;

    private final RateLimiter rateLimiter;

    public RedditTemplate() {
        rateLimiter = RateLimiter.create(0.25);
    }

    public JsonNode getUserInfo() {
        rateLimiter.acquire();
        return redditRestTemplate.getForObject("https://oauth.reddit.com/api/v1/me", JsonNode.class);
    }

    public JsonNode submitPost(final MultiValueMap<String, String> params) {
        rateLimiter.acquire();
        return redditRestTemplate.postForObject("https://oauth.reddit.com/api/submit", params, JsonNode.class);
    }

    public JsonNode searchForLink(final String url, final String subreddit) {
        rateLimiter.acquire();
        return redditRestTemplate.getForObject("https://oauth.reddit.com/r/" + subreddit + "/search?q=url:" + url + "&restrict_sr=on", JsonNode.class);
    }

    public JsonNode subredditNameSearch(final String query) {
        rateLimiter.acquire();
        final MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
        param.add("query", query);
        return redditRestTemplate.postForObject("https://oauth.reddit.com//api/search_reddit_names", param, JsonNode.class);
    }

    public boolean needsCaptcha() {
        rateLimiter.acquire();
        final String result = redditRestTemplate.getForObject("https://oauth.reddit.com/api/needs_captcha.json", String.class);
        return result.equalsIgnoreCase("true");
    }

    public String getNewCaptcha() {
        rateLimiter.acquire();
        final Map<String, String> param = new HashMap<String, String>();
        param.put("api_type", "json");
        return redditRestTemplate.postForObject("https://oauth.reddit.com/api/new_captcha", param, String.class, param);
    }

    public OAuth2AccessToken getAccessToken() {
        rateLimiter.acquire();
        return redditRestTemplate.getAccessToken();
    }

    public void setAccessToken(final OAuth2AccessToken token) {
        rateLimiter.acquire();
        redditRestTemplate.getOAuth2ClientContext().setAccessToken(token);
    }

}
