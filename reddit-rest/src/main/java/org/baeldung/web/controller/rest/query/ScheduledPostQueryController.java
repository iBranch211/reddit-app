package org.baeldung.web.controller.rest.query;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.baeldung.persistence.model.Post;
import org.baeldung.persistence.model.SubmissionResponse;
import org.baeldung.persistence.model.User;
import org.baeldung.security.UserPrincipal;
import org.baeldung.service.query.IScheduledPostQueryService;
import org.baeldung.web.controller.rest.PagingInfo;
import org.baeldung.web.controller.rest.SubmissionResponseDto;
import org.baeldung.web.dto.query.ScheduledPostDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/scheduledPosts")
class ScheduledPostQueryController {
    private final static String POST_URL_TEMPLATE = "http://www.reddit.com/r/%s/comments/%s";

    @Autowired
    private IScheduledPostQueryService scheduledPostService;

    @Autowired
    private ModelMapper modelMapper;

    public ScheduledPostQueryController() {
        super();
    }

    // API - read

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public final List<ScheduledPostDto> getScheduledPosts(@RequestParam(value = "page", required = false, defaultValue = "0") final int page, @RequestParam(value = "size", required = false, defaultValue = "10") final int size,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") final String sortDir, @RequestParam(value = "sort", required = false, defaultValue = "title") final String sort, final HttpServletResponse response) {
        final User user = getCurrentUser();
        final PagingInfo pagingInfo = new PagingInfo(page, size, scheduledPostService.countScheduledPostsByUser(user));
        response.addHeader("PAGING_INFO", pagingInfo.toString());
        final List<Post> posts = scheduledPostService.getPostsList(user, page, size, sortDir, sort);
        System.out.println(posts);
        return posts.stream().map(post -> convertToDto(post)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    @ResponseBody
    public final ScheduledPostDto getPost(@PathVariable("uuid") final String uuid) {
        return convertToDto(scheduledPostService.getPostByUuid(uuid));
    }

    @RequestMapping(value = "/available")
    @ResponseBody
    public final String countAvailablePostsToSchedule(final HttpServletRequest request) {
        if (request.isUserInRole("POST_UNLIMITED_PRIVILEGE")) {
            return "You can schedule as many posts as you want";
        }
        return "You can schedule maximium " + scheduledPostService.countAvailablePostsToSchedule(getCurrentUser()) + " posts to be submitted today";
    }

    //

    private ScheduledPostDto convertToDto(final Post post) {
        final ScheduledPostDto postDto = modelMapper.map(post, ScheduledPostDto.class);
        postDto.setSubmissionDate(post.getSubmissionDate(), getCurrentUser().getPreference().getTimezone());

        final List<SubmissionResponse> response = post.getSubmissionsResponse();
        if (response != null && response.size() > 0) {
            if (post.getMinScoreRequired() == 0) { // resubmit options not activated - only one attempt
                postDto.setStatus(response.get(0).getContent());
            } else {
                postDto.setStatus(response.get(response.size() - 1).toString().substring(0, 30));
            }
            final List<SubmissionResponseDto> responsedto = post.getSubmissionsResponse().stream().map(res -> generateResponseDto(res)).collect(Collectors.toList());
            postDto.setDetailedStatus(responsedto);
        } else {
            postDto.setStatus("Not sent yet");
            postDto.setDetailedStatus(Collections.emptyList());
        }

        postDto.setOld(post.isSent() && post.getNoOfAttempts() < 1);
        postDto.setPostRedditUrl(post.getRedditID() == null ? "#" : String.format(POST_URL_TEMPLATE, post.getSubreddit(), post.getRedditID()));

        return postDto;
    }

    private SubmissionResponseDto generateResponseDto(final SubmissionResponse responseEntity) {
        final SubmissionResponseDto dto = modelMapper.map(responseEntity, SubmissionResponseDto.class);
        final String timezone = getCurrentUser().getPreference().getTimezone();
        dto.setLocalSubmissionDate(responseEntity.getSubmissionDate(), timezone);
        if (responseEntity.getScoreCheckDate() != null) {
            dto.setLocalScoreCheckDate(responseEntity.getScoreCheckDate(), timezone);
        }
        return dto;
    }

    //
    private User getCurrentUser() {
        final UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipal.getUser();
    }

}
