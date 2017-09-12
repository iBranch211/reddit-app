package org.baeldung.config.root;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "org.baeldung.common" })
public class CommonConfig {

    public CommonConfig() {
        super();
    }

}
