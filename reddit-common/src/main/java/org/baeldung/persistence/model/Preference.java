package org.baeldung.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Preference implements IEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String email;

    private String subreddit;

    private boolean sendReplies;

    private int noOfAttempts;

    private int checkAfterInterval;

    private int submitAfterInterval;

    private int minScoreRequired;

    private int minTotalVotes;

    private boolean keepIfHasComments;

    private boolean deleteAfterLastAttempt;

    @Column(nullable = false)
    private String timezone;

    private boolean sendEmailReplies;

    public Preference() {
        super();
    }

    //

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
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

    //

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + checkAfterInterval;
        result = (prime * result) + (deleteAfterLastAttempt ? 1231 : 1237);
        result = (prime * result) + ((email == null) ? 0 : email.hashCode());
        result = (prime * result) + ((id == null) ? 0 : id.hashCode());
        result = (prime * result) + (keepIfHasComments ? 1231 : 1237);
        result = (prime * result) + minScoreRequired;
        result = (prime * result) + minTotalVotes;
        result = (prime * result) + noOfAttempts;
        result = (prime * result) + (sendEmailReplies ? 1231 : 1237);
        result = (prime * result) + (sendReplies ? 1231 : 1237);
        result = (prime * result) + submitAfterInterval;
        result = (prime * result) + ((subreddit == null) ? 0 : subreddit.hashCode());
        result = (prime * result) + ((timezone == null) ? 0 : timezone.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Preference other = (Preference) obj;
        if (checkAfterInterval != other.checkAfterInterval) {
            return false;
        }
        if (deleteAfterLastAttempt != other.deleteAfterLastAttempt) {
            return false;
        }
        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (keepIfHasComments != other.keepIfHasComments) {
            return false;
        }
        if (minScoreRequired != other.minScoreRequired) {
            return false;
        }
        if (minTotalVotes != other.minTotalVotes) {
            return false;
        }
        if (noOfAttempts != other.noOfAttempts) {
            return false;
        }
        if (sendEmailReplies != other.sendEmailReplies) {
            return false;
        }
        if (sendReplies != other.sendReplies) {
            return false;
        }
        if (submitAfterInterval != other.submitAfterInterval) {
            return false;
        }
        if (subreddit == null) {
            if (other.subreddit != null) {
                return false;
            }
        } else if (!subreddit.equals(other.subreddit)) {
            return false;
        }
        if (timezone == null) {
            if (other.timezone != null) {
                return false;
            }
        } else if (!timezone.equals(other.timezone)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Preference [id=").append(id).append(", email=").append(email).append(", subreddit=").append(subreddit).append(", sendReplies=").append(sendReplies).append(", noOfAttempts=").append(noOfAttempts).append(", checkAfterInterval=")
                .append(checkAfterInterval).append(", submitAfterInterval=").append(submitAfterInterval).append(", minScoreRequired=").append(minScoreRequired).append(", minTotalVotes=").append(minTotalVotes).append(", keepIfHasComments=")
                .append(keepIfHasComments).append(", deleteAfterLastAttempt=").append(deleteAfterLastAttempt).append(", timezone=").append(timezone).append(", sendEmailReplies=").append(sendEmailReplies).append("]");
        return builder.toString();
    }

}
