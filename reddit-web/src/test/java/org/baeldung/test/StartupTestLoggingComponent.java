package org.baeldung.test;

import java.util.List;

import org.baeldung.common.Env;
import org.baeldung.web.live.config.CommonPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class StartupTestLoggingComponent implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String ENV_KEY = "envTarget";

    @Autowired
    private Environment env;

    @Autowired
    private CommonPaths paths;

    public StartupTestLoggingComponent() {
        super();
    }

    //

    @Override
    public void afterPropertiesSet() {
        logger.info("============================================================================");
        try {
            logEnvTarget(env);
            logLiveTestData(env);
        } catch (final Exception ex) {
            logger.warn("There was a problem logging data on startup", ex);
        }

        logger.info("============================================================================");
    }

    // UTIL

    private void logEnvTarget(final Environment environment) {
        final String envTarget = getValueOfProperty(environment, ENV_KEY, Env.LOCAL.getName(), Env.envs());
        logger.info("{} = {}", ENV_KEY, envTarget);
    }

    private void logLiveTestData(final Environment environment) {
        final String serverRoot = paths.getServerRoot();
        logger.info("Base live test URL = {}", serverRoot);
    }

    //

    private final String getValueOfProperty(final Environment environment, final String propertyKey, final String propertyDefaultValue, final List<String> acceptablePropertyValues) {
        String propValue = environment.getProperty(propertyKey);
        if (propValue == null) {
            propValue = propertyDefaultValue;
            logger.info("The {} doesn't have an explicit value; default value is = {}", propertyKey, propertyDefaultValue);
        }

        if (acceptablePropertyValues != null) {
            if (!acceptablePropertyValues.contains(propValue)) {
                logger.warn("The property = {} has an invalid value = {}", propertyKey, propValue);
            }
        }

        if (propValue == null) {
            logger.warn("The property = {} is null", propertyKey);
        }

        return propValue;
    }

}
