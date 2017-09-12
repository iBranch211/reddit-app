package org.baeldung.service.impl;

import static org.junit.Assert.assertTrue;

import org.baeldung.persistence.model.Post;
import org.baeldung.reddit.persistence.service.impl.PostRedditService;
import org.baeldung.reddit.util.PostScores;
import org.junit.Test;

public class PostScoreDetailedReasonUnitTest extends PostRedditService {

    @Test
    public void whenHasEnoughScore_thenSucceed() {
        final Post post = new Post();
        post.setMinScoreRequired(5);
        final PostScores postScores = new PostScores(6, 10, 1);

        assertTrue(getSuccessReason(post, postScores).contains("Succeed because score"));
    }

    @Test
    public void whenHasEnoughTotalVotes_thenSucceed() {
        final Post post = new Post();
        post.setMinScoreRequired(5);
        post.setMinTotalVotes(8);
        final PostScores postScores = new PostScores(2, 10, 1);

        assertTrue(getSuccessReason(post, postScores).contains("Succeed because total votes"));
    }

    @Test
    public void givenKeepPostIfHasComments_whenHasComments_thenSucceed() {
        final Post post = new Post();
        post.setMinScoreRequired(5);
        post.setKeepIfHasComments(true);
        final PostScores postScores = new PostScores(2, 10, 1);

        assertTrue(getSuccessReason(post, postScores).contains("Succeed because has comments"));
    }

    @Test
    public void whenNotEnoughScore_thenFail() {
        final Post post = new Post();
        post.setMinScoreRequired(5);
        final PostScores postScores = new PostScores(2, 10, 1);

        assertTrue(getFailReason(post, postScores).contains("Failed because score"));
    }

    @Test
    public void whenNotEnoughTotalVotes_thenFail() {
        final Post post = new Post();
        post.setMinScoreRequired(5);
        post.setMinTotalVotes(15);
        final PostScores postScores = new PostScores(2, 10, 1);

        final String reason = getFailReason(post, postScores);
        assertTrue(reason.contains("Failed because score"));
        assertTrue(reason.contains("and total votes"));
    }

    @Test
    public void givenKeepPostIfHasComments_whenNotHasComments_thenFail() {
        final Post post = new Post();
        post.setMinScoreRequired(5);
        post.setKeepIfHasComments(true);
        final PostScores postScores = new PostScores(2, 10, 0);

        final String reason = getFailReason(post, postScores);
        assertTrue(reason.contains("Failed because score"));
        assertTrue(reason.contains("and has no comments"));
    }
}
