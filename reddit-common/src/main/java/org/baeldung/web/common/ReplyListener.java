package org.baeldung.web.common;

import org.baeldung.reddit.util.OnNewPostReplyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class ReplyListener implements ApplicationListener<OnNewPostReplyEvent> {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Override
    public void onApplicationEvent(final OnNewPostReplyEvent event) {
        final SimpleMailMessage email = constructEmailMessage(event);
        mailSender.send(email);
    }

    private SimpleMailMessage constructEmailMessage(final OnNewPostReplyEvent event) {
        final String recipientAddress = event.getEmail();
        final String subject = "New Post Replies";
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(event.getContent());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

}
