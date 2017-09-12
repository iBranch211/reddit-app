package org.baeldung.web.dto.command;

import org.baeldung.reddit.util.Dto;

public class FeedUpdateCommandDto implements Dto, ICommandDto {
    private Long id;
    private String name;
    private String url;

    public FeedUpdateCommandDto() {
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

}
