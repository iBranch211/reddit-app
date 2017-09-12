package org.baeldung.web.controller.rest;

import org.baeldung.reddit.util.Dto;

public class PreferenceDto implements Dto {

    private Long id;

    private String subreddit;

    private boolean sendReplies;

    private boolean sendEmailReplies;

    private int noOfAttempts;

    private int checkAfterInterval;

    private int submitAfterInterval;

    private int minScoreRequired;

    private int minTotalVotes;

    private boolean keepIfHasComments;

    private boolean deleteAfterLastAttempt;

    private String timezone;

    public PreferenceDto() {
        super();
    }

    //

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(final String subreddit) {
        this.subreddit = subreddit;
    }

    public boolean isSendReplies() {
        return sendReplies;
    }

    public void setSendReplies(final boolean sendReplies) {
        this.sendReplies = sendReplies;
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

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(final String timezone) {
        this.timezone = timezone;
    }

    public boolean isSendEmailReplies() {
        return sendEmailReplies;
    }

    public void setSendEmailReplies(final boolean sendEmailReplies) {
        this.sendEmailReplies = sendEmailReplies;
    }

}
