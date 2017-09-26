package org.baeldung.web.controller.rest;

import java.io.Serializable;

import javax.servlet.http.HttpServletResponse;

import org.baeldung.web.common.ApiError;
import org.baeldung.web.exceptions.FeedServerException;
import org.baeldung.web.exceptions.InvalidDateException;
import org.baeldung.web.exceptions.InvalidOldPasswordException;
import org.baeldung.web.exceptions.InvalidResubmitOptionsException;
import org.baeldung.web.exceptions.InvalidUsername;
import org.baeldung.web.exceptions.UserNotFoundException;
import org.baeldung.web.exceptions.UsernameAlreadyExistsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.client.resource.UserApprovalRequiredException;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler implements Serializable {

    private static final long serialVersionUID = -3861125729653781371L;

    public RestExceptionHandler() {
        super();
    }

    // API
    // 4xx

    @ExceptionHandler({ OAuth2AccessDeniedException.class })
    public ResponseEntity<Object> handleOAuth2AccessDeniedException(final OAuth2AccessDeniedException ex, final WebRequest request) {
        logger.error("403 Status Code", ex);
        final ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ AuthenticationCredentialsNotFoundException.class, AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(final Exception ex, final WebRequest request) {
        logger.error("403 Status Code", ex);
        final ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ HttpClientErrorException.class })
    public ResponseEntity<Object> handleHttpClientErrorException(final HttpClientErrorException ex, final WebRequest request) {
        logger.error("400 Status Code", ex);
        final ApiError apiError = new ApiError(ex.getStatusCode(), ex);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), ex.getStatusCode());
    }

    @ExceptionHandler({ InvalidDateException.class })
    public ResponseEntity<Object> handleInvalidDate(final RuntimeException ex, final WebRequest request) {
        logger.error("400 Status Code " + ex.getLocalizedMessage());
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ InvalidOldPasswordException.class })
    public ResponseEntity<Object> handleInvalidOldPassword(final RuntimeException ex, final WebRequest request) {
        logger.error("400 Status Code" + ex.getLocalizedMessage());
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ InvalidResubmitOptionsException.class })
    public ResponseEntity<Object> handleInvalidResubmitOptions(final RuntimeException ex, final WebRequest request) {
        logger.error("400 Status Code" + ex.getLocalizedMessage());
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ UsernameAlreadyExistsException.class })
    public ResponseEntity<Object> handleUsernameAlreadyExists(final RuntimeException ex, final WebRequest request) {
        logger.error("400 Status Code" + ex.getLocalizedMessage());
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ InvalidUsername.class })
    public ResponseEntity<Object> handleInvalidUsername(final RuntimeException ex, final WebRequest request) {
        logger.error("400 Status Code" + ex.getLocalizedMessage());
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ UserNotFoundException.class })
    public ResponseEntity<Object> handleUserNotFound(final RuntimeException ex, final WebRequest request) {
        logger.error("400 Status Code" + ex.getLocalizedMessage());
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity<Object> handleIllegalArgumentException(final RuntimeException ex, final WebRequest request) {
        logger.error("400 Status Code", ex);
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.error("400 Status Code", ex);
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    // 500

    @ExceptionHandler({ UserApprovalRequiredException.class, UserRedirectRequiredException.class })
    public ResponseEntity<Object> handleRedirect(final RuntimeException ex, final WebRequest request) {
        logger.info(ex.getLocalizedMessage());
        throw ex;
    }

    @ExceptionHandler({ FeedServerException.class })
    public ResponseEntity<Object> handleFeed(final RuntimeException ex, final WebRequest request) {
        logger.error("500 Status Code", ex);
        final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ MailAuthenticationException.class })
    public ResponseEntity<Object> handleMail(final RuntimeException ex, final WebRequest request) {
        logger.error("500 Status Code", ex);
        final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request, final HttpServletResponse response) {
        logger.error("500 Status Code", ex);
        final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
