package org.baeldung.web.schedule;

import java.util.List;

import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.model.Preference;
import org.baeldung.reddit.persistence.service.INotificationRedditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationRedditScheduler {

    @Autowired
    private INotificationRedditService notificationRedditService;

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void checkInboxUnread() {
        final List<Preference> preferences = preferenceRepository.findBySendEmailRepliesTrue();
        for (final Preference preference : preferences) {
            notificationRedditService.checkAndNotify(preference);
        }
    }

}
