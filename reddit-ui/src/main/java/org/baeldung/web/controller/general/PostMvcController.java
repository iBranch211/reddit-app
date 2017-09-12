package org.baeldung.web.controller.general;

import org.baeldung.persistence.model.User;
import org.baeldung.security.UserPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostMvcController {

    @RequestMapping("/postSchedule")
    public final String showSchedulePostForm(final Model model) {
        if (getCurrentUser().getAccessToken() == null) {
            model.addAttribute("msg", "Sorry, You did not connect your account to Reddit yet");
            return "submissionResponse";
        }

        final boolean isCaptchaNeeded = getCurrentUser().isCaptchaNeeded();
        if (isCaptchaNeeded) {
            model.addAttribute("msg", "Sorry, You do not have enought karma");
            return "submissionResponse";
        }
        return "schedulePostForm";
    }

    @PreAuthorize("@resourceSecurityService.isPostOwner(#uuid)")
    @RequestMapping(value = "/scheduledPost/{uuid}", method = RequestMethod.GET)
    public String showEditPostForm(@PathVariable("uuid") final String uuid) {
        return "editPostForm";
    }

    //

    private User getCurrentUser() {
        final UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipal.getUser();
    }

}
