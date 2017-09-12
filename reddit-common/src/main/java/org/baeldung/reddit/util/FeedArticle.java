package org.baeldung.reddit.util;

import java.util.Date;

public class FeedArticle {
    private String title;
    private String link;
    private Date publishDate;

    public FeedArticle() {
        super();
    }

    public FeedArticle(String title, String link, Date publishDate) {
        super();
        this.title = title;
        this.link = link;
        this.publishDate = publishDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SiteArticle [title=").append(title).append(", link=").append(link).append(", publishDate=").append(publishDate).append("]");
        return builder.toString();
    }

}
