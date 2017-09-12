package org.baeldung.web.common;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionListener implements HttpSessionListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void sessionCreated(final HttpSessionEvent event) {
        logger.info("==== Session is created ====");
        event.getSession().setMaxInactiveInterval(30 * 60);
    }

    @Override
    public void sessionDestroyed(final HttpSessionEvent event) {
        logger.info("==== Session is destroyed ====");
    }
}