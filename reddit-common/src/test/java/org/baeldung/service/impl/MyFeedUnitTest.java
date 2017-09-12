package org.baeldung.service.impl;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.baeldung.persistence.model.MyFeed;
import org.baeldung.reddit.util.FeedArticle;
import org.baeldung.service.impl.query.MyFeedQueryService;
import org.baeldung.service.query.IMyFeedQueryService;
import org.junit.Before;
import org.junit.Test;

public class MyFeedUnitTest {

    private IMyFeedQueryService service;

    @Before
    public void init() {
        service = new MyFeedQueryService();
    }

    @Test
    public void whenUsingServiceToReadWordpressFeed_thenCorrect() {
        final MyFeed site = new MyFeed("http://www.baeldung.com/feed/");
        final List<FeedArticle> articles = service.getArticlesFromSite(site);

        assertNotNull(articles);
        for (final FeedArticle article : articles) {
            assertNotNull(article.getTitle());
            assertNotNull(article.getLink());
            System.out.println("Title: " + article.getTitle() + ", Link: " + article.getLink());
        }
    }

    @Test
    public void whenUsingRomeToReadBloggerFeed_thenCorrect() {
        final MyFeed site = new MyFeed("http://dohaesam.blogspot.com/feeds/posts/default");
        final List<FeedArticle> articles = service.getArticlesFromSite(site);

        assertNotNull(articles);
        for (final FeedArticle article : articles) {
            assertNotNull(article.getTitle());
            assertNotNull(article.getLink());
            System.out.println("Title: " + article.getTitle() + ", Link: " + article.getLink());
        }
    }

}
