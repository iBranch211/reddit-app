package org.baeldung.config.root;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableAsync
@ComponentScan({ "org.baeldung.web.common", "org.baeldung.web.exceptions", "org.baeldung.web.metric", "org.baeldung.web.schedule" })
public class WebGeneralConfig {

}
