package org.baeldung.web.controller.rest.command;

import javax.servlet.http.HttpServletRequest;

import org.baeldung.persistence.model.User;
import org.baeldung.security.UserPrincipal;
import org.baeldung.service.command.IUserCommandService;
import org.baeldung.web.dto.command.UserRegisterCommandDto;
import org.baeldung.web.dto.command.UserTriggerResetPasswordCommandDto;
import org.baeldung.web.dto.command.UserUpdateCommandDto;
import org.baeldung.web.dto.command.UserUpdatePasswordCommandDto;
import org.baeldung.web.dto.command.UserchangePasswordCommandDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/users")
public class UserCommandController {

    @Autowired
    private IUserCommandService userService;

    @Autowired
    private ModelMapper modelMapper;

    //

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void register(final HttpServletRequest request, @RequestBody final UserRegisterCommandDto userDto) {
        final String appUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "") + request.getContextPath();
        userService.registerNewUser(userDto.getUsername(), userDto.getEmail(), userDto.getPassword(), appUrl);
    }

    /**
     * used to update current user password
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/password", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateUserPassword(@RequestBody final UserUpdatePasswordCommandDto userDto) {
        userService.updateUserPassword(getCurrentUser(), userDto.getPassword(), userDto.getOldPassword());
    }

    /**
     * used to trigger reset password by sending email with reset password token
     */
    @RequestMapping(value = "/passwordReset", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void triggerResetPassword(final HttpServletRequest request, @RequestBody final UserTriggerResetPasswordCommandDto userDto) {
        final String appUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "") + request.getContextPath();
        userService.resetPassword(userDto.getEmail(), appUrl);
    }

    /**
     * used to change user password – this command is called after user use password reset token.
     */
    @RequestMapping(value = "/password", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void changeUserPassword(@RequestBody final UserchangePasswordCommandDto userDto) {
        userService.changeUserPassword(getCurrentUser(), userDto.getPassword());
    }

    // admin

    @PreAuthorize("hasRole('USER_WRITE_PRIVILEGE')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestBody final UserUpdateCommandDto userDto) {
        userService.updateUser(convertToEntity(userDto));
    }

    //

    private User convertToEntity(final UserUpdateCommandDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    private User getCurrentUser() {
        final UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipal.getUser();
    }
}
