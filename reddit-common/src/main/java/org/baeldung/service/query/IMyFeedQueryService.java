package org.baeldung.service.query;

import java.util.List;

import org.baeldung.persistence.model.MyFeed;
import org.baeldung.persistence.model.User;
import org.baeldung.reddit.util.FeedArticle;

public interface IMyFeedQueryService {

    List<MyFeed> getFeedsByUser(final User user, int page, int size, String sortDir, String sort);

    long countFeedsByUser(User user);

    MyFeed findFeedById(final Long feedId);

    List<FeedArticle> getArticlesFromFeed(final Long feedId);

    List<FeedArticle> getArticlesFromSite(final MyFeed feed);

}
