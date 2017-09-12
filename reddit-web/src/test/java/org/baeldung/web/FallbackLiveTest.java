package org.baeldung.web;

import static org.junit.Assert.assertEquals;

import org.baeldung.web.live.AbstractBaseLiveTest;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class FallbackLiveTest extends AbstractBaseLiveTest {

    @Test
    public void givenDummyUrl_whenRequestingUsingGet_thenCorrect() {
        assertEquals(200, givenAuth().get(urlPrefix + "/dummy123").statusCode());
    }

    @Test
    public void givenDummyUrl_whenRequestingUsingPost_thenCorrect() {
        assertEquals(200, givenAuth().post(urlPrefix + "/dummy123").statusCode());
    }

    @Test
    public void givenDummyUrl_whenRequestingUsingPut_thenCorrect() {
        assertEquals(200, givenAuth().put(urlPrefix + "/dummy123").statusCode());
    }

    @Test
    public void givenDummyUrl_whenRequestingUsingDelete_thenCorrect() {
        assertEquals(200, givenAuth().delete(urlPrefix + "/dummy123").statusCode());
    }

    @Test
    public void givenDummyUrl_whenRequestingUsingHead_thenCorrect() {
        assertEquals(200, givenAuth().head(urlPrefix + "/dummy123").statusCode());
    }

    @Test
    public void givenDummyUrl_whenRequestingUsingPatch_thenCorrect() {
        assertEquals(200, givenAuth().patch(urlPrefix + "/dummy123").statusCode());
    }

    @Test
    public void givenDummyUrl_whenRequestingUsingOptions_thenCorrect() {
        assertEquals(200, givenAuth().options(urlPrefix + "/dummy123").statusCode());
    }
}
