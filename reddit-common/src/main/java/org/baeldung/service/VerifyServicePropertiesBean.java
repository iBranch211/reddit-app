package org.baeldung.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class VerifyServicePropertiesBean implements PriorityOrdered, BeanFactoryPostProcessor {

    private static String[] requiredProperties = new String[] { // @formatter:off
        "smtp.host", "smtp.port", "smtp.protocol", "smtp.username", "smtp.password", "support.email",
        "jdbc.driverClassName", "jdbc.url", "jdbc.user", "jdbc.pass", "init-db", "hibernate.dialect", "hibernate.show_sql", "hibernate.hbm2ddl.auto",
        "reddit.clientID","reddit.clientSecret","reddit.accessTokenUri", "reddit.userAuthorizationUri","reddit.redirectUri"
    }; // @formatter:on

    public VerifyServicePropertiesBean() {
        super();
    }

    // API

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
        final Environment environment = beanFactory.getBean(Environment.class);

        for (final String requiredProp : requiredProperties) {
            if (environment.getProperty(requiredProp) == null) {
                throw new ApplicationContextException("The following required property is missing: " + requiredProp);
            }
        }

        if ((environment.getProperty("envTarget") == "production") && (environment.getProperty("hibernate.hbm2ddl.auto") == "create-drop")) {
            throw new ApplicationContextException("hibernate.hbm2ddl.auto can not be create-drop for production");
        }
    }

}
