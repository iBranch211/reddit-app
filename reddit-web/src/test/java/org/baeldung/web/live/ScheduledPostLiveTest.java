package org.baeldung.web.live;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.baeldung.web.dto.command.ScheduledPostAddCommandDto;
import org.baeldung.web.dto.query.ScheduledPostDto;
import org.junit.Test;


public class ScheduledPostLiveTest extends AbstractLiveTest<ScheduledPostDto> {
    private static final String DATE = "00-01-01 00:00";

    // test

    @Test
    public void whenScheduleANewPost_thenCreated() throws ParseException, IOException {
        final ScheduledPostAddCommandDto post = new ScheduledPostAddCommandDto();
        post.setTitle(randomAlphabetic(6));
        post.setUrl("test.com");
        post.setSubreddit(randomAlphabetic(6));
        post.setDate("3" + randomNumeric(1) + DATE);

        final RequestSpecification givenAuth = givenAuth();
        final Response response = withRequestBody(givenAuth, post).post(urlPrefix + "/api/scheduledPosts");

        assertEquals(201, response.statusCode());
        final ScheduledPostDto result = objectMapper.reader().forType(ScheduledPostDto.class).readValue(response.asString());
        assertEquals(result.getUrl(), post.getUrl());
    }

    @Test
    public void whenGettingUserScheduledPosts_thenCorrect() throws ParseException, IOException {
        newDto();

        final Response response = givenAuth().get(urlPrefix + "/api/scheduledPosts");

        assertEquals(200, response.statusCode());
        assertTrue(response.as(List.class).size() > 0);
    }

    @Test
    public void whenUpdatingScheduledPost_thenUpdated() throws ParseException, IOException {
        final ScheduledPostDto post = newDto();

        post.setTitle("new title");
        Response response = withRequestBody(givenAuth(), post).put(urlPrefix + "/api/scheduledPosts/" + post.getUuid());

        assertEquals(200, response.statusCode());
        response = givenAuth().get(urlPrefix + "/api/scheduledPosts/" + post.getUuid());
        assertTrue(response.asString().contains(post.getTitle()));
    }

    @Test
    public void whenDeletingScheduledPost_thenDeleted() throws ParseException, IOException {
        final ScheduledPostDto post = newDto();
        final Response response = givenAuth().delete(urlPrefix + "/api/scheduledPosts/" + post.getUuid());

        assertEquals(204, response.statusCode());
    }

    // pagination

    @Test
    public void givenMoreThanOnePage_whenGettingUserScheduledPosts_thenNextPageExist() throws ParseException, IOException {
        newDto();
        newDto();
        newDto();

        final Response response = givenAuth().params("page", 0, "size", 2).get(urlPrefix + "/api/scheduledPosts");

        assertEquals(200, response.statusCode());
        assertTrue(response.as(List.class).size() > 0);

        final String pagingInfo = response.getHeader("PAGING_INFO");
        final long totalNoRecords = Long.parseLong(pagingInfo.split(",")[0].split("=")[1]);
        final String uriToNextPage = pagingInfo.split(",")[2].replace("uriToNextPage=", "").trim();

        assertTrue(totalNoRecords > 2);
        assertEquals(uriToNextPage, "page=1&size=2");
    }

    @Test
    public void givenMoreThanOnePage_whenGettingUserScheduledPostsForSecondPage_thenCorrect() throws ParseException, IOException {
        newDto();
        newDto();
        newDto();

        final Response response = givenAuth().params("page", 1, "size", 2).get(urlPrefix + "/api/scheduledPosts");

        assertEquals(200, response.statusCode());
        assertTrue(response.as(List.class).size() > 0);

        final String pagingInfo = response.getHeader("PAGING_INFO");
        final long totalNoRecords = Long.parseLong(pagingInfo.split(",")[0].split("=")[1]);
        final String uriToPrevPage = pagingInfo.split(",")[3].replace("uriToPrevPage=", "").trim();

        assertTrue(totalNoRecords > 2);
        assertEquals(uriToPrevPage, "page=0&size=2");
    }

    //

    @Override
    protected ScheduledPostDto newDto() throws ParseException, IOException {
        final ScheduledPostAddCommandDto post = new ScheduledPostAddCommandDto();
        post.setTitle(randomAlphabetic(6));
        post.setUrl("test.com");
        post.setSubreddit(randomAlphabetic(6));
        post.setDate("3" + randomNumeric(1) + DATE);
        post.setResubmitOptionsActivated(false);
        final Response response = withRequestBody(givenAuth(), post).post(urlPrefix + "/api/scheduledPosts");
        return objectMapper.reader().forType(ScheduledPostDto.class).readValue(response.asString());
    }
}
