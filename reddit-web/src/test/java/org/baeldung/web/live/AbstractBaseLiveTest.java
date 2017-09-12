package org.baeldung.web.live;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.filter.session.SessionFilter;
import io.restassured.specification.RequestSpecification;

import java.text.SimpleDateFormat;

import org.baeldung.config.TestConfig;
import org.baeldung.web.live.config.CommonPaths;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public abstract class AbstractBaseLiveTest {
    public final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SessionFilter sessionFilter = new SessionFilter();

    private static boolean isAuthenticated;

    @Autowired
    private CommonPaths commonPaths;

    protected String urlPrefix;

    protected ObjectMapper objectMapper = new ObjectMapper().setDateFormat(dateFormat);

    @Before
    public void setup() {
        urlPrefix = commonPaths.getServerRoot();
    }

    //

    protected RequestSpecification givenAuth() {
        if (isAuthenticated) {
            return RestAssured.given()
                .filter(sessionFilter);
        }
        isAuthenticated = true;
        final FormAuthConfig formConfig = new FormAuthConfig("/reddit-web/j_spring_security_check", "username", "password");
        return RestAssured.given()
            .auth()
            .form("john", "123", formConfig)
            .filter(sessionFilter);

    }

    protected RequestSpecification withRequestBody(final RequestSpecification req, final Object obj) throws JsonProcessingException {
        return req.contentType(MediaType.APPLICATION_JSON_VALUE).body(objectMapper.writeValueAsString(obj));
    }

}
