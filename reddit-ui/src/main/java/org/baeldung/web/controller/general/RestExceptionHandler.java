package org.baeldung.web.controller.general;

import java.io.Serializable;

import org.springframework.security.oauth2.client.resource.UserApprovalRequiredException;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler implements Serializable {

    private static final long serialVersionUID = -3365045939814599316L;

    public RestExceptionHandler() {
        super();
    }

    @ExceptionHandler({ UserApprovalRequiredException.class, UserRedirectRequiredException.class })
    public String handleRedirect(final RuntimeException ex, final WebRequest request) {
        logger.info(ex.getLocalizedMessage());
        throw ex;
    }

    @ExceptionHandler({ Exception.class })
    public String handleInternal(final RuntimeException ex, final WebRequest request) {
        logger.error(ex);
        final String response = "Error Occurred: " + ex.getMessage();
        return "redirect:/submissionResponse?msg=" + response;
    }
}
