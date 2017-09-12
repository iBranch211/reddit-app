package org.baeldung.persistence.dao;

import org.baeldung.persistence.model.Post;
import org.baeldung.persistence.model.SubmissionResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionResponseRepository extends JpaRepository<SubmissionResponse, Long> {

    SubmissionResponse findOneByPostAndAttemptNumber(Post post, int attemptNumber);
}
