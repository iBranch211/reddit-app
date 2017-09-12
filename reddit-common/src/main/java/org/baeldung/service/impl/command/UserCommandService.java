package org.baeldung.service.impl.command;

import java.util.TimeZone;
import java.util.UUID;

import org.baeldung.persistence.dao.PasswordResetTokenRepository;
import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.dao.RoleRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.dao.VerificationTokenRepository;
import org.baeldung.persistence.model.PasswordResetToken;
import org.baeldung.persistence.model.Preference;
import org.baeldung.persistence.model.Role;
import org.baeldung.persistence.model.User;
import org.baeldung.persistence.model.VerificationToken;
import org.baeldung.service.OnRegistrationCompleteEvent;
import org.baeldung.service.command.IUserCommandService;
import org.baeldung.web.exceptions.InvalidOldPasswordException;
import org.baeldung.web.exceptions.UserNotFoundException;
import org.baeldung.web.exceptions.UsernameAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

@Service
public class UserCommandService implements IUserCommandService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private Environment env;

    @Override
    public void registerNewUser(final String username, final String email, final String password, final String appUrl) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        final Preference pref = new Preference();
        pref.setTimezone(TimeZone.getDefault().getID());
        pref.setEmail(email);
        preferenceRepository.save(pref);
        user.setPreference(pref);
        final Role role = roleRepository.findByName("ROLE_USER");
        user.setRoles(Sets.newHashSet(role));
        user.setEnabled(false);
        userRepository.save(user);
        logger.info("Register new User {}", user);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, appUrl));
    }

    @Override
    public void updateUserPassword(final User user, final String password, final String oldPassword) {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidOldPasswordException("Invalid old password");
        }
        changeUserPassword(user, password);
    }

    @Override
    public void changeUserPassword(final User user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public void resetPassword(final String userEmail, final String appUrl) {
        final Preference preference = preferenceRepository.findByEmail(userEmail);
        final User user = userRepository.findByPreference(preference);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        final String token = UUID.randomUUID().toString();
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
        final SimpleMailMessage email = constructResetTokenEmail(appUrl, token, user);
        mailSender.send(email);
    }

    @Override
    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public void updateUser(final User user) {
        final User oldUser = userRepository.findOne(user.getId());
        oldUser.setEnabled(user.isEnabled());
        oldUser.setRoles(user.getRoles());
        System.out.println(oldUser);
        userRepository.save(oldUser);
    }

    //
    private final SimpleMailMessage constructResetTokenEmail(final String contextPath, final String token, final User user) {
        final String url = contextPath + "/users/passwordReset?id=" + user.getId() + "&token=" + token;
        final String message = "Reset your password from ";
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getPreference().getEmail());
        email.setSubject("Reset Password");
        email.setText(message + " \r\n" + url);
        email.setFrom(env.getProperty("support.email"));
        logger.info("Reset password Url : {}", email.getText());
        return email;
    }

}
