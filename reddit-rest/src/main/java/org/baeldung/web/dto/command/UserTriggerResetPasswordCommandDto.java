package org.baeldung.web.dto.command;

import org.baeldung.reddit.util.Dto;

public class UserTriggerResetPasswordCommandDto implements Dto, ICommandDto {

    private String email;

    public UserTriggerResetPasswordCommandDto() {
        super();
    }

    //

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

}
