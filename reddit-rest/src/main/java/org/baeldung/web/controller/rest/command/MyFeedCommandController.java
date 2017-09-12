package org.baeldung.web.controller.rest.command;

import org.baeldung.persistence.model.MyFeed;
import org.baeldung.persistence.model.User;
import org.baeldung.security.UserPrincipal;
import org.baeldung.service.command.IMyFeedCommanndService;
import org.baeldung.web.dto.command.FeedAddCommandDto;
import org.baeldung.web.dto.command.FeedUpdateCommandDto;
import org.baeldung.web.dto.query.FeedDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/myFeeds")
class MyFeedCommandController {

    @Autowired
    private IMyFeedCommanndService myFeedService;

    @Autowired
    private ModelMapper modelMapper;

    // === API Methods

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public FeedDto addFeed(@RequestBody final FeedAddCommandDto feedDto) {
        final MyFeed feed = convertToEntity(feedDto);
        feed.setUser(getCurrentUser());
        return convertToQueryDto(myFeedService.addFeed(feed));
    }

    @PreAuthorize("@resourceSecurityService.isRssFeedOwner(#feedDto.id)")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateFeed(@RequestBody final FeedUpdateCommandDto feedDto) {
        final MyFeed feed = modelMapper.map(feedDto, MyFeed.class);
        feed.setUser(getCurrentUser());
        myFeedService.updateFeed(feed);
    }

    @PreAuthorize("@resourceSecurityService.isRssFeedOwner(#id)")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFeed(@PathVariable("id") final Long id) {
        myFeedService.deleteFeedById(id);
    }

    //

    private FeedDto convertToQueryDto(final MyFeed feed) {
        return modelMapper.map(feed, FeedDto.class);
    }

    private MyFeed convertToEntity(final FeedAddCommandDto feed) {
        return modelMapper.map(feed, MyFeed.class);
    }

    private User getCurrentUser() {
        final UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipal.getUser();
    }

}
