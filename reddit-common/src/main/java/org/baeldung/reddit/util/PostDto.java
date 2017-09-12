package org.baeldung.reddit.util;

import javax.validation.constraints.NotNull;

public class PostDto implements Dto {

    @NotNull
    private String title;

    @NotNull
    private String url;

    @NotNull
    private String subreddit;

    private boolean sendreplies;

    private String iden;
    private String captcha;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(final String sr) {
        this.subreddit = sr;
    }

    public boolean isSendreplies() {
        return sendreplies;
    }

    public void setSendreplies(final boolean sendreplies) {
        this.sendreplies = sendreplies;
    }

    public String getIden() {
        return iden;
    }

    public void setIden(final String iden) {
        this.iden = iden;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(final String captcha) {
        this.captcha = captcha;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("PostDto [title=").append(title).append(", url=").append(url).append(", sr=").append(subreddit).append(", sendreplies=").append(sendreplies).append(", iden=").append(iden).append(", captcha=").append(captcha).append("]");
        return builder.toString();
    }

}
