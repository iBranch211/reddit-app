package org.baeldung.service.impl.command;

import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;

import org.apache.commons.lang.time.DateUtils;
import org.baeldung.persistence.dao.PostRepository;
import org.baeldung.persistence.model.Post;
import org.baeldung.persistence.model.User;
import org.baeldung.service.command.IScheduledPostCommandService;
import org.baeldung.web.exceptions.InvalidDateException;
import org.baeldung.web.exceptions.InvalidResubmitOptionsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

@Service
public class ScheduledPostCommandService implements IScheduledPostCommandService {
    private static final int LIMIT_SCHEDULED_POSTS_PER_DAY = 3;

    @Autowired
    private PostRepository postRepository;

    //

    @Override
    public Post schedulePost(final User user, final boolean isSuperUser, final Post post, final boolean resubmitOptionsActivated) {
        Preconditions.checkArgument(post.getMinScoreRequired() >= 0, "Minimum Score can not be negative");
        Preconditions.checkArgument(post.getMinTotalVotes() >= 0, "Minimum Total votes can not be negative");
        if (resubmitOptionsActivated && !checkIfValidResubmitOptions(post)) {
            throw new InvalidResubmitOptionsException("Invalid Resubmit Options");
        }
        if (post.getSubmissionDate().before(new Date())) {
            throw new InvalidDateException("Scheduling Date already passed");
        }
        if (!(isSuperUser || checkIfCanSchedule(user, post.getSubmissionDate()))) {
            throw new InvalidDateException("Scheduling Date exceeds daily limit");
        }
        post.setUser(user);
        return postRepository.save(post);
    }

    @Override
    public void updatePost(final boolean isSuperUser, final Post post, final boolean resubmitOptionsActivated) {
        Preconditions.checkArgument(post.getMinScoreRequired() >= 0, "Minimum Score can not be negative");
        Preconditions.checkArgument(post.getMinTotalVotes() >= 0, "Minimum Total votes can not be negative");

        final Post oldPost = postRepository.findByUuid(post.getUuid());

        if (resubmitOptionsActivated && !checkIfValidResubmitOptions(post)) {
            throw new InvalidResubmitOptionsException("Invalid Resubmit Options");
        }
        if (post.getSubmissionDate().before(new Date())) {
            throw new InvalidDateException("Scheduling Date already passed");
        }
        if (!(isSuperUser || checkIfCanSchedule(oldPost.getUser(), post.getSubmissionDate()))) {
            throw new InvalidDateException("Scheduling Date exceeds daily limit");
        }
        post.setId(oldPost.getId());
        post.setRedditID(oldPost.getRedditID());
        post.setSent(oldPost.isSent());
        post.setSubmissionsResponse(post.getSubmissionsResponse());
        post.setUser(oldPost.getUser());
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void deletePostByUuid(final String uuid) {
        postRepository.deleteByUuid(uuid);
    }

    //

    private boolean checkIfValidResubmitOptions(final Post post) {
        if (checkIfAllNonZero(post.getNoOfAttempts(), post.getCheckAfterInterval(), post.getSubmitAfterInterval(), post.getMinScoreRequired())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkIfAllNonZero(final int... args) {
        for (final int tmp : args) {
            if (tmp == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfCanSchedule(final User user, final Date date) {
        final Date start = DateUtils.truncate(date, Calendar.DATE);
        final Date end = DateUtils.addDays(start, 1);
        final long count = postRepository.countByUserAndSubmissionDateBetween(user, start, end);
        return count < LIMIT_SCHEDULED_POSTS_PER_DAY;
    }

}
