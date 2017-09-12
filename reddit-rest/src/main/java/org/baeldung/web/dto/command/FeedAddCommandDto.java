package org.baeldung.web.dto.command;

import org.baeldung.reddit.util.Dto;

public class FeedAddCommandDto implements Dto, ICommandDto {

    private String name;
    private String url;

    public FeedAddCommandDto() {
        super();
    }

    public FeedAddCommandDto(final String name, final String url) {
        super();
        this.name = name;
        this.url = url;
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
