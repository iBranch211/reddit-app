package org.baeldung.web.live;

import static org.junit.Assert.assertEquals;
import io.restassured.response.Response;

import org.baeldung.web.controller.rest.PreferenceDto;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

public class UserPreferenceLiveTest extends AbstractBaseLiveTest {

    // tests

    @Test
    public void whenGettingPrefernce_thenCorrect() {
        final Response response = givenAuth().get(urlPrefix + "/api/user/preference");

        assertEquals(200, response.statusCode());
    }

    @Test
    public void whenUpdattingPrefernce_thenCorrect() throws JsonProcessingException {
        final PreferenceDto pref = givenAuth().get(urlPrefix + "/api/user/preference").as(PreferenceDto.class);
        pref.setSubreddit("java");
        Response response = withRequestBody(givenAuth(), pref).put(urlPrefix + "/api/user/preference/" + pref.getId());

        assertEquals(200, response.statusCode());
        response = givenAuth().get(urlPrefix + "/api/user/preference");

        assertEquals(response.as(PreferenceDto.class).getSubreddit(), pref.getSubreddit());
    }
}
