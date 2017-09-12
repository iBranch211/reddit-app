package org.baeldung.persistence;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.baeldung.persistence.model.Privilege;
import org.baeldung.persistence.model.Role;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class SetupData {

    public final static String USERS_FILE = "setupData/users.csv";
    public final static String ROLES_FILE = "setupData/roles.csv";
    public final static String PRIVILEGES_FILE = "setupData/privileges.csv";
    public final static String ROLES_PRIVILEGES_FILE = "setupData/roles_privileges.csv";
    public final static String USERS_ROLES_FILE = "setupData/users_roles.csv";

    @Autowired
    private CsvDataLoader csvDataLoader;

    public List<Privilege> getPrivileges() {
        return csvDataLoader.loadObjectList(Privilege.class, PRIVILEGES_FILE);
    }

    public List<Role> getRoles() {
        final List<Privilege> allPrivileges = getPrivileges();
        final List<Role> roles = csvDataLoader.loadObjectList(Role.class, ROLES_FILE);
        final List<String[]> rolesPrivileges = csvDataLoader.loadManyToManyRelationship(SetupData.ROLES_PRIVILEGES_FILE);

        for (final String[] rolePrivilege : rolesPrivileges) {
            final Role role = findRoleByName(roles, rolePrivilege[0]);
            Set<Privilege> privileges = role.getPrivileges();
            if (privileges == null) {
                privileges = new HashSet<Privilege>();
            }
            privileges.add(findPrivilegeByName(allPrivileges, rolePrivilege[1]));
            role.setPrivileges(privileges);
        }
        return roles;
    }

    public List<User> getUsers() {
        final List<Role> allRoles = getRoles();
        final List<User> users = csvDataLoader.loadObjectList(User.class, SetupData.USERS_FILE);
        final List<String[]> usersRoles = csvDataLoader.loadManyToManyRelationship(SetupData.USERS_ROLES_FILE);

        for (final String[] userRole : usersRoles) {
            final User user = findByUserByUsername(users, userRole[0]);
            Set<Role> roles = user.getRoles();
            if (roles == null) {
                roles = new HashSet<Role>();
            }
            roles.add(findRoleByName(allRoles, userRole[1]));
            user.setRoles(roles);
        }
        return users;
    }

    //

    private Privilege findPrivilegeByName(final List<Privilege> allPrivileges, final String privilegeName) {
        return allPrivileges.stream().filter(item -> item.getName().equals(privilegeName)).findFirst().get();
    }

    private Role findRoleByName(final List<Role> roles, final String roleName) {
        return roles.stream().filter(item -> item.getName().equals(roleName)).findFirst().get();
    }

    private User findByUserByUsername(final List<User> users, final String username) {
        return users.stream().filter(item -> item.getUsername().equals(username)).findFirst().get();
    }

}
