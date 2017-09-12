package org.baeldung.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    @Qualifier("redditRestTemplate")
    private OAuth2RestTemplate redditRestTemplate;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication auth) throws IOException, ServletException {
        final User user = ((UserPrincipal) auth.getPrincipal()).getUser();
        if (user.getAccessToken() != null) {
            final DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(user.getAccessToken());
            token.setRefreshToken(new DefaultOAuth2RefreshToken((user.getRefreshToken())));
            token.setExpiration(user.getTokenExpiration());
            redditRestTemplate.getOAuth2ClientContext().setAccessToken(token);
        }

        final boolean isAdmin = user.getRoles().stream().filter(role -> role.getName().contains("ADMIN")).count() != 0;
        if (isAdmin) {
            response.sendRedirect("adminHome");
        } else {
            response.sendRedirect("home");
        }
    }
}
