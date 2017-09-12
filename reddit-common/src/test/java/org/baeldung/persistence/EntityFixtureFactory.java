package org.baeldung.persistence;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import org.baeldung.persistence.model.MyFeed;
import org.baeldung.persistence.model.User;

public class EntityFixtureFactory {

    // API

    public static final MyFeed newSite(final User user) {
        final MyFeed site = new MyFeed();
        site.setUrl(randomAlphabetic(6));
        site.setName(randomAlphabetic(6));
        site.setUser(user);
        return site;
    }

    public static final User newUser() {
        final User user = new User();
        user.setUsername(randomAlphabetic(6));
        return user;
    }

}
