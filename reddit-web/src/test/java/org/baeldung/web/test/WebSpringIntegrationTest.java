package org.baeldung.web.test;

import org.baeldung.config.root.PersistenceJpaConfig;
import org.baeldung.config.root.RedditConfig;
import org.baeldung.config.root.ServiceConfig;
import org.baeldung.config.root.WebGeneralConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceJpaConfig.class, ServiceConfig.class, WebGeneralConfig.class, RedditConfig.class })
@WebAppConfiguration
public class WebSpringIntegrationTest {

    @Test
    public final void whenContextIsBootstrapped_thenOk() {
        //
    }

}
