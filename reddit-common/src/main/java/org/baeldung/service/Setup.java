package org.baeldung.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.baeldung.persistence.SetupData;
import org.baeldung.persistence.model.Privilege;
import org.baeldung.persistence.model.Role;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Setup {

    @Autowired
    private SetupData setupData;

    @Autowired
    private ISetupService setupService;

    @PostConstruct
    private void setupData() {
        setupRolesAndPrivileges();
        setupUsers();
        updateOldPosts();
    }

    private void setupRolesAndPrivileges() {
        final List<Privilege> privileges = setupData.getPrivileges();
        for (final Privilege privilege : privileges) {
            setupService.setupPrivilege(privilege);
        }

        final List<Role> roles = setupData.getRoles();
        for (final Role role : roles) {
            setupService.setupRole(role);
        }
    }

    private void setupUsers() {
        final List<User> users = setupData.getUsers();
        for (final User user : users) {
            setupService.setupUser(user);
        }
    }

    private void updateOldPosts() {
        setupService.generateUuidForOldPosts();
    }

}
