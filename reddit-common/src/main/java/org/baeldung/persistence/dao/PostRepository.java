package org.baeldung.persistence.dao;

import java.util.Date;
import java.util.List;

import org.baeldung.persistence.model.Post;
import org.baeldung.persistence.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findBySubmissionDateBeforeAndIsSent(final Date date, final boolean isSent);

    Page<Post> findByUser(final User user, final Pageable pageable);

    List<Post> findByRedditIDNotNullAndNoOfAttemptsGreaterThan(final int attempts);

    List<Post> findByRedditIDNotNullAndNoOfAttemptsAndDeleteAfterLastAttemptTrue(final int attempts);

    List<Post> findByUser(final User user);

    Long countByUserAndSubmissionDateBetween(final User user, Date start, Date end);

    Long countByUser(final User user);

    Post findByUuid(String uuid);

    Long deleteByUuid(String uuid);

    List<Post> findByUuidIsNull();

}