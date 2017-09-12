package org.baeldung.service.impl.query;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.baeldung.persistence.dao.MyFeedRepository;
import org.baeldung.persistence.model.MyFeed;
import org.baeldung.persistence.model.User;
import org.baeldung.reddit.util.FeedArticle;
import org.baeldung.service.query.IMyFeedQueryService;
import org.baeldung.web.exceptions.FeedServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

@Service
public class MyFeedQueryService implements IMyFeedQueryService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MyFeedRepository myFeedRepository;

    // API

    @Override
    public List<MyFeed> getFeedsByUser(final User user, final int page, final int size, final String sortDir, final String sort) {
        final PageRequest pageReq = new PageRequest(page, size, Sort.Direction.fromString(sortDir), sort);
        final Page<MyFeed> feeds = myFeedRepository.findByUser(user, pageReq);
        return feeds.getContent();
    }

    @Override
    public long countFeedsByUser(final User user) {
        return myFeedRepository.countByUser(user);
    }

    @Override
    public MyFeed findFeedById(final Long feedId) {
        return myFeedRepository.findOne(feedId);
    }

    @Override
    public List<FeedArticle> getArticlesFromFeed(final Long feedId) {
        final MyFeed feed = myFeedRepository.findOne(feedId);
        return getArticlesFromSite(feed);
    }

    @Override
    public List<FeedArticle> getArticlesFromSite(final MyFeed feed) {
        List<SyndEntry> entries;
        try {
            entries = getFeedEntries(feed.getUrl());
        } catch (final Exception e) {
            throw new FeedServerException("Error Occurred while parsing feed", e);
        }
        return parseFeed(entries);
    }

    // Non API

    private List<SyndEntry> getFeedEntries(final String feedUrl) throws IllegalArgumentException, FeedException, IOException {
        final URL url = new URL(feedUrl);
        final SyndFeed feed = new SyndFeedInput().build(new XmlReader(url));
        logger.info("Read feed from url : {}", feedUrl);
        final List<SyndEntry> entries = feed.getEntries();
        logger.info("{} entries extracted from url {}", entries.size(), feedUrl);
        return entries;
    }

    private List<FeedArticle> parseFeed(final List<SyndEntry> entries) {
        final List<FeedArticle> articles = new ArrayList<FeedArticle>();
        for (final SyndEntry entry : entries) {
            articles.add(new FeedArticle(entry.getTitle(), entry.getLink(), entry.getPublishedDate()));
        }
        return articles;
    }

}
