package org.baeldung.persistence.test;

import org.baeldung.common.AbstractPersistenceIntegrationTest;
import org.baeldung.config.root.PersistenceJpaConfig;
import org.baeldung.persistence.EntityFixtureFactory;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceJpaConfig.class }, loader = AnnotationConfigContextLoader.class)
public class UserIntegrationTest extends AbstractPersistenceIntegrationTest<User> {

    @Autowired
    private UserRepository repository;

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoExceptions() {
        //
    }

    // API - protected

    @Override
    protected final UserRepository getApi() {
        return repository;
    }

    @Override
    protected final void invalidate(final User entity) {
        entity.setUsername(null);
    }

    @Override
    protected final User createNewEntity() {
        return EntityFixtureFactory.newUser();
    }

}
