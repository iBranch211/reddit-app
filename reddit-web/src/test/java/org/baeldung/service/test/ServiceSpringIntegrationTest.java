package org.baeldung.service.test;

import org.baeldung.config.root.PersistenceJpaConfig;
import org.baeldung.config.root.RedditConfig;
import org.baeldung.config.root.ServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceJpaConfig.class, ServiceConfig.class, RedditConfig.class }, loader = AnnotationConfigContextLoader.class)
public class ServiceSpringIntegrationTest {

    @Test
    public final void whenContextIsBootstrapped_thenOk() {
        //
    }

}
