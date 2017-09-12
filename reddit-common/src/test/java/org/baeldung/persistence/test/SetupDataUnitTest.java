package org.baeldung.persistence.test;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.baeldung.config.root.PersistenceJpaConfig;
import org.baeldung.persistence.SetupData;
import org.baeldung.persistence.model.Privilege;
import org.baeldung.persistence.model.Role;
import org.baeldung.persistence.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceJpaConfig.class }, loader = AnnotationConfigContextLoader.class)
public class SetupDataUnitTest {
    @Autowired
    private SetupData setupData;

    @Test
    public void whenGettingUsersFromCsvFile_thenCorrect() {
        final List<User> users = setupData.getUsers();

        assertFalse(users.isEmpty());
        for (final User user : users) {
            assertFalse(user.getRoles().isEmpty());
        }
    }

    @Test
    public void whenGettingRolesFromCsvFile_thenCorrect() {
        final List<Role> roles = setupData.getRoles();

        assertFalse(roles.isEmpty());
        for (final Role role : roles) {
            assertFalse(role.getPrivileges().isEmpty());
        }
    }

    @Test
    public void whenGettingPrivilegesFromCsvFile_thenCorrect() {
        final List<Privilege> privileges = setupData.getPrivileges();
        assertFalse(privileges.isEmpty());
    }

}
