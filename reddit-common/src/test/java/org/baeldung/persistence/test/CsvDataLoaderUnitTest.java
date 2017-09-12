package org.baeldung.persistence.test;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.baeldung.config.root.PersistenceJpaConfig;
import org.baeldung.persistence.CsvDataLoader;
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
public class CsvDataLoaderUnitTest {

    @Autowired
    private CsvDataLoader csvDataLoader;

    @Test
    public void whenLoadingUsersFromCsvFile_thenLoaded() {
        final List<User> users = csvDataLoader.loadObjectList(User.class, SetupData.USERS_FILE);
        assertFalse(users.isEmpty());
    }

    @Test
    public void whenLoadingRolesFromCsvFile_thenLoaded() {
        final List<Role> roles = csvDataLoader.loadObjectList(Role.class, SetupData.ROLES_FILE);
        assertFalse(roles.isEmpty());
    }

    @Test
    public void whenLoadingPrivilegesFromCsvFile_thenLoaded() {
        final List<Privilege> privileges = csvDataLoader.loadObjectList(Privilege.class, SetupData.PRIVILEGES_FILE);
        assertFalse(privileges.isEmpty());
    }

    @Test
    public void whenLoadingUsersRolesRelationFromCsvFile_thenLoaded() {
        final List<String[]> usersRoles = csvDataLoader.loadManyToManyRelationship(SetupData.USERS_ROLES_FILE);
        assertFalse(usersRoles.isEmpty());
    }

    @Test
    public void whenLoadingRolesPrivilegesRelationFromCsvFile_thenLoaded() {
        final List<String[]> rolesPrivileges = csvDataLoader.loadManyToManyRelationship(SetupData.ROLES_PRIVILEGES_FILE);
        assertFalse(rolesPrivileges.isEmpty());
    }
}
