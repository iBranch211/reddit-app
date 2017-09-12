package org.baeldung.config.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.baeldung.config.api.WebApiConfig;
import org.baeldung.config.frontend.ThymeleafConfig;
import org.baeldung.config.frontend.WebFrontendConfig;
import org.baeldung.config.root.CommonConfig;
import org.baeldung.config.root.PersistenceJpaConfig;
import org.baeldung.config.root.RedditConfig;
import org.baeldung.config.root.ServiceConfig;
import org.baeldung.config.root.WebGeneralConfig;
import org.baeldung.web.common.SessionListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    @Bean
    public ServletRegistrationBean frontendServlet() {
        final AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(WebFrontendConfig.class, ThymeleafConfig.class);
        final ServletRegistrationBean registration = new ServletRegistrationBean(new DispatcherServlet(dispatcherContext), "/*");
        registration.setName("FrontendServlet");
        registration.setLoadOnStartup(1);
        return registration;
    }

    @Bean
    public ServletRegistrationBean apiServlet() {
        final AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(WebApiConfig.class);
        final ServletRegistrationBean registration = new ServletRegistrationBean(new DispatcherServlet(dispatcherContext), "/api/*");
        registration.setName("ApiServlet");
        registration.setLoadOnStartup(2);
        return registration;
    }

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        application.sources(Application.class, CommonConfig.class, PersistenceJpaConfig.class, RedditConfig.class, ServiceConfig.class, WebGeneralConfig.class);
        return application;
    }

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.addListener(new SessionListener());
        servletContext.addListener(new RequestContextListener());
        servletContext.addListener(new HttpSessionEventPublisher());
    }

    public static void main(final String... args) {
        SpringApplication.run(Application.class, args);
    }

}