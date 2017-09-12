package org.baeldung.persistence.dao;

import java.util.List;

import org.baeldung.persistence.model.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    Preference findByEmail(String email);

    List<Preference> findBySendEmailRepliesTrue();
}
