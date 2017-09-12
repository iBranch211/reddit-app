package org.baeldung.web.controller.general;

import org.baeldung.service.TokenState;
import org.baeldung.service.query.IUserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/users")
public class UserMvcController {

    @Autowired
    private IUserQueryService userService;

    @RequestMapping(value = "/regitrationConfirmation", method = RequestMethod.GET)
    public String confirmRegistration(final Model model, @RequestParam("token") final String token) {
        final TokenState result = userService.checkConfirmRegistrationToken(token);
        if (result == TokenState.VALID) {
            return "redirect:/?msg=Registration confirmed successfully";
        }
        model.addAttribute("msg", result.toString());
        return "submissionResponse";
    }

    @RequestMapping(value = "/passwordReset", method = RequestMethod.GET)
    public String resetPassword(final Model model, @RequestParam("id") final long id, @RequestParam("token") final String token) {
        final TokenState result = userService.checkPasswordResetToken(id, token);
        if (result == TokenState.VALID) {
            return "changePassword";
        }
        model.addAttribute("msg", result);
        return "submissionResponse";
    }
}
