package org.baeldung.reddit.util;

import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class OnNewPostReplyEvent extends ApplicationEvent {
    private final String email;
    private final String content;

    public OnNewPostReplyEvent(final String email, final String content) {
        super(email);
        this.email = email;
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public String getContent() {
        return content;
    }

}
