package org.baeldung.persistence.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.baeldung.config.root.PersistenceJpaConfig;
import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.Preference;
import org.baeldung.persistence.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceJpaConfig.class }, loader = AnnotationConfigContextLoader.class)
public class UserProfileIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferenceRepository preferenceRepository;

    private User userJohn, userTom;

    private Preference prefJohn, prefTom;

    private final String timezone = "GMT";

    @Before
    public void init() {
        prefJohn = preferenceRepository.findByEmail("john@gmail.com");
        if (prefJohn == null) {
            prefJohn = new Preference();
            prefJohn.setEmail("john@gmail.com");
            prefJohn.setTimezone(timezone);
            prefJohn = preferenceRepository.save(prefJohn);
        }

        prefTom = preferenceRepository.findByEmail("tom@gmail.com");
        if (prefTom == null) {
            prefTom = new Preference();
            prefTom.setEmail("tom@gmail.com");
            prefTom.setTimezone(timezone);
            prefTom = preferenceRepository.save(prefTom);
        }

        userJohn = userRepository.findByUsername("John");
        if (userJohn == null) {
            userJohn = new User();
            userJohn.setUsername("John");
            userJohn.setPreference(prefJohn);
            userJohn = userRepository.save(userJohn);
        }

        userTom = userRepository.findByUsername("Tom");
        if (userTom == null) {
            userTom = new User();
            userTom.setUsername("Tom");
            userTom.setPreference(prefTom);
            userTom = userRepository.save(userTom);
        }
    }

    // tests

    @Test
    public void whenGettingUserPreference_thenCorrect() {
        final String emailJohn = userJohn.getPreference().getEmail();
        final String emailTom = userTom.getPreference().getEmail();

        assertTrue(emailJohn.contains("john"));
        assertTrue(emailTom.contains("tom"));
    }

    @Test
    public void whenUpdatingUserPreference_thenCorrect() {

        prefJohn.setSubreddit("kitten");
        preferenceRepository.save(prefJohn);
        prefTom.setSubreddit("java");
        preferenceRepository.save(prefTom);

        final String srJohn = userJohn.getPreference().getSubreddit();
        final String srTom = userTom.getPreference().getSubreddit();
        assertEquals(srJohn, "kitten");
        assertEquals(srTom, "java");
    }

}
