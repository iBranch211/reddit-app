package org.baeldung.service.query;

import java.util.List;

import org.baeldung.persistence.model.Post;
import org.baeldung.persistence.model.User;

public interface IScheduledPostQueryService {

    Post getPostByUuid(String uuid);

    List<Post> getPostsList(User user, int page, int size, String sortDir, String sort);

    long countScheduledPostsByUser(User user);

    int countAvailablePostsToSchedule(User user);
}
