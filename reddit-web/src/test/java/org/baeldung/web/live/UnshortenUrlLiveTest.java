package org.baeldung.web.live;

import static org.junit.Assert.assertTrue;
import io.restassured.response.Response;

import java.io.IOException;

import org.junit.Test;


public class UnshortenUrlLiveTest extends AbstractBaseLiveTest {

    // test

    @Test
    public final void givenfeedUrl_thenCorrectResult() throws IOException {
        final String expectedResult = "http://www.baeldung.com/2015-week-review-39";
        final String actualResult = getOriginalUrl("http://feedproxy.google.com/~r/Baeldung/~3/KEQzz7yfjHU/2015-week-review-39");
        assertTrue(actualResult.equals(expectedResult));
    }

    @Test
    public final void givenShortenedMultiple_whenUrlIsUnshortened_thenCorrectResult() throws IOException {
        final String expectedResult = "http://www.baeldung.com/rest-versioning";
        final String actualResult = getOriginalUrl("http://t.co/e4rDDbnzmk");
        assertTrue(actualResult.equals(expectedResult));
    }

    //

    private String getOriginalUrl(final String url) {
        final Response response = givenAuth().get(urlPrefix + "/url/original?url=" + url);
        final String result = response.asString();
        return result.substring(1, result.length() - 1);
    }
}
