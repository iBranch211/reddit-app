package org.baeldung.persistence.dao;

import java.util.List;

import org.baeldung.persistence.model.MyFeed;
import org.baeldung.persistence.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyFeedRepository extends JpaRepository<MyFeed, Long> {

    List<MyFeed> findByUser(final User user);

    Page<MyFeed> findByUser(User user, Pageable pageable);

    long countByUser(User user);

}