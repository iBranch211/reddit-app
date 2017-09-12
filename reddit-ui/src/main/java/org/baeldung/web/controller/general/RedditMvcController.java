package org.baeldung.web.controller.general;

import org.baeldung.persistence.model.User;
import org.baeldung.reddit.persistence.beans.RedditTemplate;
import org.baeldung.reddit.persistence.service.IRedditService;
import org.baeldung.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.resource.UserApprovalRequiredException;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedditMvcController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedditTemplate redditTemplate;

    @Autowired
    private IRedditService redditService;

    // === API Methods

    @RequestMapping("/redditLogin")
    public final String redditLogin(final Model model) {
        try {
            final OAuth2AccessToken token = redditTemplate.getAccessToken();
            redditService.connectReddit(redditTemplate.needsCaptcha(), token);
            return "redirect:home";
        } catch (final Exception ex) {
            if ((ex.getClass() == UserApprovalRequiredException.class) || (ex.getClass() == UserRedirectRequiredException.class)) {
                throw ex;
            } else {
                logger.error("Error while connecting to reddit", ex);
                model.addAttribute("msg", ex.getLocalizedMessage());
                return "submissionResponse";
            }
        }
    }

    @RequestMapping("/redditReconnect")
    public final String redditReconnect() {
        redditTemplate.setAccessToken(null);
        return "redirect:redditLogin";
    }

    @RequestMapping("/post")
    public final String showSubmissionForm(final Model model) {
        if (getCurrentUser().getAccessToken() == null) {
            model.addAttribute("msg", "Sorry, You did not connect your account to Reddit yet");
            return "submissionResponse";
        }

        final boolean isCaptchaNeeded = getCurrentUser().isCaptchaNeeded();
        if (isCaptchaNeeded) {
            final String iden = redditTemplate.getNewCaptcha();
            model.addAttribute("iden", iden);
        }

        return "submissionForm";
    }

    //

    private User getCurrentUser() {
        final UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipal.getUser();
    }

}
