package org.baeldung.reddit.util;

public class PostScores {

    private int score;
    private int totalVotes;
    private int noOfComments;

    public PostScores() {
        super();
    }

    public PostScores(final int score, final int totalVotes, final int noOfComments) {
        super();
        this.score = score;
        this.totalVotes = totalVotes;
        this.noOfComments = noOfComments;
    }

    //

    public int getScore() {
        return score;
    }

    public void setScore(final int score) {
        this.score = score;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(final int totalVotes) {
        this.totalVotes = totalVotes;
    }

    public int getNoOfComments() {
        return noOfComments;
    }

    public void setNoOfComments(final int noOfComments) {
        this.noOfComments = noOfComments;
    }

    //

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("PostScore [score=").append(score).append(", totalVotes=").append(totalVotes).append(", noOfComments=").append(noOfComments).append("]");
        return builder.toString();
    }

}
