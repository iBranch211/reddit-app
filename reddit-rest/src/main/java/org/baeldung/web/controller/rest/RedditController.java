package org.baeldung.web.controller.rest;

import java.util.List;

import org.baeldung.reddit.persistence.service.IRedditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
class RedditController {

    @Autowired
    private IRedditService redditService;

    // === API Methods

    @RequestMapping(value = "/subredditAutoComplete")
    @ResponseBody
    public List<String> subredditAutoComplete(@RequestParam("term") final String term) {
        return redditService.searchSubreddit(term);
    }

    @RequestMapping(value = "/isAccessTokenValid")
    @ResponseBody
    public boolean isAccessTokenValid() {
        return redditService.isCurrentUserAccessTokenValid();
    }

}
