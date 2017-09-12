package org.baeldung.web.common;

import org.baeldung.persistence.model.Post;
import org.baeldung.reddit.util.OnPostSubmittedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SubmissionListener implements ApplicationListener<OnPostSubmittedEvent> {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Override
    public void onApplicationEvent(final OnPostSubmittedEvent event) {
        final SimpleMailMessage email = constructEmailMessage(event);
        mailSender.send(email);
    }

    private SimpleMailMessage constructEmailMessage(final OnPostSubmittedEvent event) {
        final String recipientAddress = event.getEmail();
        final String subject = "Your scheduled post submitted";
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(constructMailContent(event.getPost()));
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private String constructMailContent(final Post post) {
        return "Your post " + post.getTitle() + " is submitted.\n" + "http://www.reddit.com/r/" + post.getSubreddit() + "/comments/" + post.getRedditID();
    }

}
