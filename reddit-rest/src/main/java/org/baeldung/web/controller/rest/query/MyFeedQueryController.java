package org.baeldung.web.controller.rest.query;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.baeldung.persistence.model.MyFeed;
import org.baeldung.persistence.model.User;
import org.baeldung.reddit.util.FeedArticle;
import org.baeldung.security.UserPrincipal;
import org.baeldung.service.query.IMyFeedQueryService;
import org.baeldung.web.controller.rest.PagingInfo;
import org.baeldung.web.dto.query.FeedDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/myFeeds")
class MyFeedQueryController {

    @Autowired
    private IMyFeedQueryService myFeedService;

    @Autowired
    private ModelMapper modelMapper;

    // === API Methods

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<FeedDto> getFeedsList(@RequestParam(value = "page", required = false, defaultValue = "0") final int page, @RequestParam(value = "size", required = false, defaultValue = "10") final int size,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") final String sortDir, @RequestParam(value = "sort", required = false, defaultValue = "name") final String sort, final HttpServletResponse response) {
        final User user = getCurrentUser();
        final PagingInfo pagingInfo = new PagingInfo(page, size, myFeedService.countFeedsByUser(user));
        response.addHeader("PAGING_INFO", pagingInfo.toString());
        final List<MyFeed> feeds = myFeedService.getFeedsByUser(user, page, size, sortDir, sort);
        return feeds.stream().map(feed -> convertToDto(feed)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/articles")
    @ResponseBody
    public List<FeedArticle> getFeedArticles(@RequestParam("id") final Long feedId) {
        return myFeedService.getArticlesFromFeed(feedId);
    }

    //

    private FeedDto convertToDto(final MyFeed feed) {
        return modelMapper.map(feed, FeedDto.class);
    }

    private User getCurrentUser() {
        final UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipal.getUser();
    }

}
