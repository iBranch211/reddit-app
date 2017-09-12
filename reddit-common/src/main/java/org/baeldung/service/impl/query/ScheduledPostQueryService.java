package org.baeldung.service.impl.query;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.baeldung.persistence.dao.PostRepository;
import org.baeldung.persistence.model.Post;
import org.baeldung.persistence.model.User;
import org.baeldung.service.query.IScheduledPostQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ScheduledPostQueryService implements IScheduledPostQueryService {
    private static final int LIMIT_SCHEDULED_POSTS_PER_DAY = 3;

    @Autowired
    private PostRepository postRepository;

    //

    @Override
    public List<Post> getPostsList(final User user, final int page, final int size, final String sortDir, final String sort) {
        final PageRequest pageReq = new PageRequest(page, size, Sort.Direction.fromString(sortDir), sort);
        final Page<Post> posts = postRepository.findByUser(user, pageReq);
        return posts.getContent();
    }

    @Override
    public Post getPostByUuid(final String uuid) {
        return postRepository.findByUuid(uuid);
    }

    @Override
    public long countScheduledPostsByUser(final User user) {
        return postRepository.countByUser(user);
    }

    @Override
    public int countAvailablePostsToSchedule(final User user) {
        final Date start = DateUtils.truncate(new Date(), Calendar.DATE);
        final Date end = DateUtils.addDays(start, 1);
        final long count = postRepository.countByUserAndSubmissionDateBetween(user, start, end);
        return (int) (LIMIT_SCHEDULED_POSTS_PER_DAY - count);
    }

}
