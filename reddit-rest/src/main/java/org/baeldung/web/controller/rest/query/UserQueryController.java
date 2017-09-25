package org.baeldung.web.controller.rest.query;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.baeldung.persistence.model.User;
import org.baeldung.service.query.IScheduledPostQueryService;
import org.baeldung.service.query.IUserQueryService;
import org.baeldung.web.controller.rest.PagingInfo;
import org.baeldung.web.dto.query.UserQueryDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/users")
public class UserQueryController {

    @Autowired
    private IUserQueryService userService;

    @Autowired
    private IScheduledPostQueryService scheduledPostService;

    @Autowired
    private ModelMapper modelMapper;

    @PreAuthorize("hasAuthority('USER_READ_PRIVILEGE')")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<UserQueryDto> getUsersList(@RequestParam(value = "page", required = false, defaultValue = "0") final int page, @RequestParam(value = "size", required = false, defaultValue = "10") final int size,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") final String sortDir, @RequestParam(value = "sort", required = false, defaultValue = "username") final String sort, final HttpServletResponse response) {
        final PagingInfo pagingInfo = new PagingInfo(page, size, userService.countAllUsers());
        response.addHeader("PAGING_INFO", pagingInfo.toString());
        if (sort.equalsIgnoreCase("scheduledPostsCount")) {
            return sortByScheduledPostsCount(page, size, sortDir);
        }
        final List<User> users = userService.getUsersList(page, size, sortDir, sort);
        return users.stream().map(user -> convertUserEntityToDto(user)).collect(Collectors.toList());
    }

    //

    private List<UserQueryDto> sortByScheduledPostsCount(int page, int size, String sortDir) {
        final List<User> allUsers = userService.getAllUsers();
        final List<UserQueryDto> userDtos = allUsers.stream().map(user -> convertUserEntityToDto(user)).collect(Collectors.toList());
        if (sortDir.equalsIgnoreCase("asc")) {
            return userDtos.stream().sorted((u1, u2) -> Long.compare(u1.getScheduledPostsCount(), u2.getScheduledPostsCount())).skip(page * size).limit(size).collect(Collectors.toList());
        }
        return userDtos.stream().sorted((u1, u2) -> Long.compare(u2.getScheduledPostsCount(), u1.getScheduledPostsCount())).skip(page * size).limit(size).collect(Collectors.toList());
    }

    private UserQueryDto convertUserEntityToDto(final User user) {
        final UserQueryDto dto = modelMapper.map(user, UserQueryDto.class);
        dto.setScheduledPostsCount(scheduledPostService.countScheduledPostsByUser(user));
        return dto;
    }
}
