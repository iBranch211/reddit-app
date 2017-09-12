package org.baeldung.service.query;

import java.util.List;

import org.baeldung.persistence.model.User;
import org.baeldung.service.TokenState;

public interface IUserQueryService {

    List<User> getUsersList(int page, int size, String sortDir, String sort);

    List<User> getAllUsers();

    TokenState checkPasswordResetToken(final long userId, final String token);

    TokenState checkConfirmRegistrationToken(final String token);

    long countAllUsers();

}
