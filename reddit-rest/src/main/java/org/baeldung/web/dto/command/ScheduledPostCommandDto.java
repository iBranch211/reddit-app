package org.baeldung.web.dto.command;

import org.baeldung.reddit.util.Dto;

public class ScheduledPostCommandDto implements Dto, ICommandDto {

    private String title;

    private String subreddit;

    private String url;

    private boolean sendReplies;

    private String date;

    private int noOfAttempts;

    private int checkAfterInterval;

    private int submitAfterInterval;

    private int minScoreRequired;

    private int minTotalVotes;

    private boolean keepIfHasComments;

    private boolean deleteAfterLastAttempt;

    private boolean resubmitOptionsActivated;

    public ScheduledPostCommandDto() {
        super();
    }

    //

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(final String subreddit) {
        this.subreddit = subreddit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public boolean isSendReplies() {
        return sendReplies;
    }

    public void setSendReplies(final boolean sendReplies) {
        this.sendReplies = sendReplies;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public int getNoOfAttempts() {
        return noOfAttempts;
    }

    public void setNoOfAttempts(final int noOfAttempts) {
        this.noOfAttempts = noOfAttempts;
    }

    public int getCheckAfterInterval() {
        return checkAfterInterval;
    }

    public void setCheckAfterInterval(final int checkAfterInterval) {
        this.checkAfterInterval = checkAfterInterval;
    }

    public int getSubmitAfterInterval() {
        return submitAfterInterval;
    }

    public void setSubmitAfterInterval(final int submitAfterInterval) {
        this.submitAfterInterval = submitAfterInterval;
    }

    public int getMinScoreRequired() {
        return minScoreRequired;
    }

    public void setMinScoreRequired(final int minScoreRequired) {
        this.minScoreRequired = minScoreRequired;
    }

    public int getMinTotalVotes() {
        return minTotalVotes;
    }

    public void setMinTotalVotes(final int minTotalVotes) {
        this.minTotalVotes = minTotalVotes;
    }

    public boolean isKeepIfHasComments() {
        return keepIfHasComments;
    }

    public void setKeepIfHasComments(final boolean keepIfHasComments) {
        this.keepIfHasComments = keepIfHasComments;
    }

    public boolean isDeleteAfterLastAttempt() {
        return deleteAfterLastAttempt;
    }

    public void setDeleteAfterLastAttempt(final boolean deleteAfterLastAttempt) {
        this.deleteAfterLastAttempt = deleteAfterLastAttempt;
    }

    public boolean isResubmitOptionsActivated() {
        return resubmitOptionsActivated;
    }

    public void setResubmitOptionsActivated(final boolean resubmitOptionsActivated) {
        this.resubmitOptionsActivated = resubmitOptionsActivated;
    }

}
