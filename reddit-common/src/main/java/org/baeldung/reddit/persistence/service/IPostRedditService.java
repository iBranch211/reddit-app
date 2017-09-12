package org.baeldung.reddit.persistence.service;

import org.baeldung.persistence.model.Post;
import org.baeldung.reddit.util.PostScores;

public interface IPostRedditService {
    void submitPost(final Post post);

    void checkAndReSubmit(final Post post);

    void checkAndDelete(final Post post);

    PostScores getPostScores(final Post post);

    void deletePost(final String redditId);
}
