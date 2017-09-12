package org.baeldung.persistence.test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.util.Date;

import org.baeldung.common.AbstractPersistenceIntegrationTest;
import org.baeldung.config.root.PersistenceJpaConfig;
import org.baeldung.persistence.dao.PostRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.Post;
import org.baeldung.persistence.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceJpaConfig.class }, loader = AnnotationConfigContextLoader.class)
public class PostIntegrationTest extends AbstractPersistenceIntegrationTest<Post> {

    @Autowired
    private PostRepository repository;

    @Autowired
    private UserRepository userRepository;

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoExceptions() {
        //
    }

    // API - protected

    @Override
    protected final PostRepository getApi() {
        return repository;
    }

    @Override
    protected final void invalidate(final Post entity) {
        entity.setSubreddit(null);
    }

    @Override
    protected final Post createNewEntity() {
        final Post post = new Post();
        post.setTitle(randomAlphabetic(6));
        post.setSubreddit(randomAlphabetic(6));
        post.setUrl(randomAlphabetic(6));
        post.setSubmissionDate(new Date());
        User user = userRepository.findByUsername("john");
        if (user == null) {
            user = new User("john");
            userRepository.save(user);
        }
        post.setUser(user);
        return post;
    }

}
