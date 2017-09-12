package org.baeldung.persistence.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class SubmissionResponse implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int attemptNumber;

    private String content;

    private Date submissionDate;

    private Date scoreCheckDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public SubmissionResponse() {
        super();
    }

    public SubmissionResponse(final int attemptNumber, final String content, final Post post) {
        super();
        this.attemptNumber = attemptNumber;
        this.content = content;
        this.submissionDate = new Date();
        this.post = post;
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

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(final Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Date getScoreCheckDate() {
        return scoreCheckDate;
    }

    public void setScoreCheckDate(final Date scoreCheckDate) {
        this.scoreCheckDate = scoreCheckDate;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(final Post post) {
        this.post = post;
    }

    //

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Attempt No ").append(attemptNumber).append(": ").append(content);
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + attemptNumber;
        result = (prime * result) + ((content == null) ? 0 : content.hashCode());
        result = (prime * result) + ((id == null) ? 0 : id.hashCode());
        result = (prime * result) + ((post == null) ? 0 : post.hashCode());
        result = (prime * result) + ((scoreCheckDate == null) ? 0 : scoreCheckDate.hashCode());
        result = (prime * result) + ((submissionDate == null) ? 0 : submissionDate.hashCode());
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
        final SubmissionResponse other = (SubmissionResponse) obj;
        if (attemptNumber != other.attemptNumber) {
            return false;
        }
        if (content == null) {
            if (other.content != null) {
                return false;
            }
        } else if (!content.equals(other.content)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (post == null) {
            if (other.post != null) {
                return false;
            }
        } else if (!post.equals(other.post)) {
            return false;
        }
        if (scoreCheckDate == null) {
            if (other.scoreCheckDate != null) {
                return false;
            }
        } else if (!scoreCheckDate.equals(other.scoreCheckDate)) {
            return false;
        }
        if (submissionDate == null) {
            if (other.submissionDate != null) {
                return false;
            }
        } else if (!submissionDate.equals(other.submissionDate)) {
            return false;
        }
        return true;
    }

}
