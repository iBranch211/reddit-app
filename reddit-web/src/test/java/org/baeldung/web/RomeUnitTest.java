package org.baeldung.web;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.Test;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

public class RomeUnitTest {

    @Test
    public void whenUsingRomeToReadWordpressFeed_thenCorrect() throws IllegalArgumentException, FeedException, IOException {
        final URL url = new URL("http://www.baeldung.com/feed/");
        final SyndFeed feed = new SyndFeedInput().build(new XmlReader(url));
        final List<SyndEntry> entries = feed.getEntries();
        assertTrue(entries.size() > 0);
        for (final SyndEntry entry : entries) {
            assertNotNull(entry.getTitle());
            assertNotNull(entry.getLink());
            System.out.println("Title: " + entry.getTitle() + ", Link: " + entry.getLink());
        }
    }

    @Test
    public void whenUsingRomeToReadBloggerFeed_thenCorrect() throws IllegalArgumentException, FeedException, IOException {
        final URL url = new URL("http://dohaesam.blogspot.com/feeds/posts/default");
        final SyndFeed feed = new SyndFeedInput().build(new XmlReader(url));
        final List<SyndEntry> entries = feed.getEntries();
        assertTrue(entries.size() > 0);
        for (final SyndEntry entry : entries) {
            assertNotNull(entry.getTitle());
            assertNotNull(entry.getLink());
            System.out.println("Title: " + entry.getTitle() + ", Link: " + entry.getLink());
        }
    }

}
