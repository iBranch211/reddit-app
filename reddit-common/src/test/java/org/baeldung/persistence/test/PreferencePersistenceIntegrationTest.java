package org.baeldung.persistence.test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import org.baeldung.common.AbstractPersistenceIntegrationTest;
import org.baeldung.config.root.PersistenceJpaConfig;
import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.model.Preference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceJpaConfig.class }, loader = AnnotationConfigContextLoader.class)
public class PreferencePersistenceIntegrationTest extends AbstractPersistenceIntegrationTest<Preference> {

    @Autowired
    private PreferenceRepository preferenceRepository;

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoExceptions() {
        //
    }

    // API - protected

    @Override
    protected final PreferenceRepository getApi() {
        return preferenceRepository;
    }

    @Override
    protected final void invalidate(final Preference entity) {
        entity.setTimezone(null);
    }

    @Override
    protected final Preference createNewEntity() {
        final Preference preference = new Preference();
        preference.setEmail(randomAlphabetic(6) + "@gmail.com");
        preference.setSubreddit("java");
        preference.setTimezone("GMT");
        return preference;
    }

}
