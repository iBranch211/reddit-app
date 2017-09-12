package org.baeldung.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import org.baeldung.persistence.dao.PostRepository;
import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.dao.PrivilegeRepository;
import org.baeldung.persistence.dao.RoleRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.Post;
import org.baeldung.persistence.model.Preference;
import org.baeldung.persistence.model.Privilege;
import org.baeldung.persistence.model.Role;
import org.baeldung.persistence.model.User;
import org.baeldung.service.ISetupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SetupService implements ISetupService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    @Transactional
    public void setupUser(final User user) {
        try {
            setupUserInternal(user);
        } catch (final Exception e) {
            logger.error("Error occurred while saving user " + user.toString(), e);
        }

    }

    @Override
    public void setupPrivilege(final Privilege privilege) {
        if (privilegeRepository.findByName(privilege.getName()) == null) {
            privilegeRepository.save(privilege);
        }
    }

    @Override
    public void setupRole(final Role role) {
        if (roleRepository.findByName(role.getName()) == null) {
            final Set<Privilege> privileges = role.getPrivileges();
            final Set<Privilege> persistedPrivileges = new HashSet<Privilege>();
            for (final Privilege privilege : privileges) {
                persistedPrivileges.add(privilegeRepository.findByName(privilege.getName()));
            }
            role.setPrivileges(persistedPrivileges);
            roleRepository.save(role);
        }
    }

    //
    private void setupUserInternal(final User user) {
        if (userRepository.findByUsername(user.getUsername()) == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setPreference(createSimplePreference(user));
            final Set<Role> roles = user.getRoles();
            final Set<Role> persistedRoles = new HashSet<Role>();
            for (final Role role : roles) {
                persistedRoles.add(roleRepository.findByName(role.getName()));
            }
            user.setRoles(persistedRoles);
            user.setEnabled(true);
            userRepository.save(user);
        }
    }

    private Preference createSimplePreference(final User user) {
        final Preference pref = new Preference();
        pref.setId(user.getId());
        pref.setTimezone(TimeZone.getDefault().getID());
        pref.setEmail(user.getUsername() + "@test.com");
        return preferenceRepository.save(pref);
    }

    @Override
    @Transactional
    public void generateUuidForOldPosts() {
        final List<Post> posts = postRepository.findByUuidIsNull();
        for (final Post post : posts) {
            post.setUuid(UUID.randomUUID().toString());
            postRepository.save(post);
        }

    }

}
