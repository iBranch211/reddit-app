package org.baeldung.reddit.persistence.service;

import org.baeldung.persistence.model.Preference;

public interface INotificationRedditService {

    void checkAndNotify(Preference preference);
}
