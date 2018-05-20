package org.baeldung.service.impl.command;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.baeldung.persistence.dao.MyFeedRepository;
import org.baeldung.persistence.model.MyFeed;
import org.baeldung.service.command.IMyFeedCommanndService;
import org.baeldung.web.exceptions.FeedServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

@Service
class MyFeedCommandService implements IMyFeedCommanndService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MyFeedRepository myFeedRepository;

    // API

    @Override
    public MyFeed addFeed(final MyFeed feed) {
        if (!isValidFeedUrl(feed.getUrl())) {
            throw new FeedServerException("Invalid Feed Url");
        }
        logger.info("New feed {} added", feed.toString());
        return myFeedRepository.save(feed);
    }

    @Override
    public void updateFeed(final MyFeed feed) {
        if (!isValidFeedUrl(feed.getUrl())) {
            throw new FeedServerException("Invalid Feed Url");
        }
        myFeedRepository.save(feed);
    }

    @Override
    public void deleteFeedById(final Long feedId) {
        myFeedRepository.deleteById(feedId);
    }

    // Non API

    private boolean isValidFeedUrl(final String feedUrl) {
        try {
            return getFeedEntries(feedUrl).size() > 0;
        } catch (final Exception e) {
            logger.error("Invalid feed url", e);
            return false;
        }
    }

    private List<SyndEntry> getFeedEntries(final String feedUrl) throws IllegalArgumentException, FeedException, IOException {
        final URL url = new URL(feedUrl);
        final SyndFeed feed = new SyndFeedInput().build(new XmlReader(url));
        logger.info("Read feed from url : {}", feedUrl);
        final List<SyndEntry> entries = feed.getEntries();
        logger.info("{} entries extracted from url {}", entries.size(), feedUrl);
        return entries;
    }

}
