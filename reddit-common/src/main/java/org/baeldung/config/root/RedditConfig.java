package org.baeldung.config.root;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.baeldung.reddit.util.UserAgentInterceptor;
import org.baeldung.security.MyAuthorizationCodeAccessTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan({ "org.baeldung.reddit.persistence" })
public class RedditConfig {

    @Bean
    public OAuth2RestTemplate schedulerRedditTemplate(final OAuth2ProtectedResourceDetails reddit) {
        final List<ClientHttpRequestInterceptor> list = new ArrayList<ClientHttpRequestInterceptor>();
        list.add(new UserAgentInterceptor());
        final OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(reddit);
        restTemplate.setInterceptors(list);
        final AccessTokenProviderChain accessTokenProvider = new AccessTokenProviderChain(
                Arrays.<AccessTokenProvider> asList(new MyAuthorizationCodeAccessTokenProvider(), new ImplicitAccessTokenProvider(), new ResourceOwnerPasswordAccessTokenProvider(), new ClientCredentialsAccessTokenProvider()));
        restTemplate.setAccessTokenProvider(accessTokenProvider);
        return restTemplate;
    }

    @Bean
    public RestTemplate simpleRestTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        final List<ClientHttpRequestInterceptor> list = new ArrayList<ClientHttpRequestInterceptor>();
        list.add(new UserAgentInterceptor());
        restTemplate.setInterceptors(list);
        return restTemplate;
    }

    @Bean
    public RedditProperties redditProperties() {
        return new RedditProperties();
    }

    @Configuration
    @EnableOAuth2Client
    @PropertySource("classpath:reddit-${envTarget:local}.properties")
    protected static class ResourceConfiguration {

        @Autowired
        private RedditProperties redditProperties;

        @Bean
        public OAuth2ProtectedResourceDetails reddit() {
            final AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
            details.setId("reddit");
            details.setClientId(redditProperties.getClientID());
            details.setClientSecret(redditProperties.getClientSecret());
            details.setAccessTokenUri(redditProperties.getAccessTokenUri());
            details.setUserAuthorizationUri(redditProperties.getUserAuthorizationUri());
            details.setTokenName("oauth_token");
            details.setScope(Arrays.asList("identity", "read", "submit", "edit", "privatemessages"));
            details.setGrantType("authorization_code");
            details.setPreEstablishedRedirectUri(redditProperties.getRedirectUri());
            details.setUseCurrentUri(false);
            return details;
        }

        @Bean
        public OAuth2RestTemplate redditRestTemplate(final OAuth2ClientContext clientContext) {
            final OAuth2RestTemplate template = new OAuth2RestTemplate(reddit(), clientContext);
            final List<ClientHttpRequestInterceptor> list = new ArrayList<ClientHttpRequestInterceptor>();
            list.add(new UserAgentInterceptor());
            template.setInterceptors(list);
            final AccessTokenProviderChain accessTokenProvider = new AccessTokenProviderChain(
                    Arrays.<AccessTokenProvider> asList(new MyAuthorizationCodeAccessTokenProvider(), new ImplicitAccessTokenProvider(), new ResourceOwnerPasswordAccessTokenProvider(), new ClientCredentialsAccessTokenProvider()));
            template.setAccessTokenProvider(accessTokenProvider);
            return template;
        }

    }
}
