package org.baeldung.service;

import org.baeldung.persistence.model.Privilege;
import org.baeldung.persistence.model.Role;
import org.baeldung.persistence.model.User;

public interface ISetupService {
    void setupUser(final User user);

    void setupPrivilege(Privilege privilege);

    void setupRole(Role role);

    void generateUuidForOldPosts();

}
