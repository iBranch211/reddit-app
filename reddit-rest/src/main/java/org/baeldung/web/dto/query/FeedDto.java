package org.baeldung.web.dto.query;

import org.baeldung.persistence.model.User;
import org.baeldung.reddit.util.Dto;

public class FeedDto implements Dto {

    private Long id;
    private String name;
    private String url;
    private User user;

    public FeedDto() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

}
