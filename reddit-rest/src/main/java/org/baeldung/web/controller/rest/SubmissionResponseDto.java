package org.baeldung.web.controller.rest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.baeldung.reddit.util.Dto;

public class SubmissionResponseDto implements Dto {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private int attemptNumber;

    private String content;

    private String localSubmissionDate;

    private String localScoreCheckDate;

    public SubmissionResponseDto() {
        super();
    }

    //

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(final int attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getLocalSubmissionDate() {
        return localSubmissionDate;
    }

    public void setLocalSubmissionDate(final String localSubmissionDate) {
        this.localSubmissionDate = localSubmissionDate;
    }

    public String getLocalScoreCheckDate() {
        return localScoreCheckDate;
    }

    public void setLocalScoreCheckDate(final String localScoreCheckDate) {
        this.localScoreCheckDate = localScoreCheckDate;
    }

    public void setLocalSubmissionDate(final Date date, final String timezone) {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        this.localSubmissionDate = dateFormat.format(date);
    }

    public void setLocalScoreCheckDate(final Date date, final String timezone) {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        this.localScoreCheckDate = dateFormat.format(date);
    }
}
