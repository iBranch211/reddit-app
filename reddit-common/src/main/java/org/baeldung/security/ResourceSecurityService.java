package org.baeldung.security;

import org.baeldung.persistence.dao.MyFeedRepository;
import org.baeldung.persistence.dao.PostRepository;
import org.baeldung.persistence.model.MyFeed;
import org.baeldung.persistence.model.Post;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("resourceSecurityService")
public class ResourceSecurityService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MyFeedRepository feedRepository;

    public boolean isPostOwner(final String uuid) {
        final UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final User user = userPrincipal.getUser();
        final Post post = postRepository.findByUuid(uuid);
        return post.getUser().getId().equals(user.getId());
    }

    public boolean isRssFeedOwner(final Long feedId) {
        final UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final User user = userPrincipal.getUser();
        final MyFeed feed = feedRepository.findOne(feedId);
        return feed.getUser().getId().equals(user.getId());
    }
}
