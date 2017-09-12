package org.baeldung.reddit.util;

import org.baeldung.persistence.model.Post;
import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class OnPostSubmittedEvent extends ApplicationEvent {
    private final Post post;
    private final String email;

    public OnPostSubmittedEvent(final Post post, final String email) {
        super(post);
        this.post = post;
        this.email = email;
    }

    public Post getPost() {
        return post;
    }

    public String getEmail() {
        return email;
    }

}
