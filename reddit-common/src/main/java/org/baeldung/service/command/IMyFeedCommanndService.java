package org.baeldung.service.command;

import org.baeldung.persistence.model.MyFeed;

public interface IMyFeedCommanndService {

    MyFeed addFeed(final MyFeed feed);

    void updateFeed(final MyFeed feed);

    void deleteFeedById(final Long feedId);

}
