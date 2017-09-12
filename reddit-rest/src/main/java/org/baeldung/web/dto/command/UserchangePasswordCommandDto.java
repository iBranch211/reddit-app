package org.baeldung.web.dto.command;

import org.baeldung.reddit.util.Dto;

public class UserchangePasswordCommandDto implements Dto, ICommandDto {

    private String password;

    public UserchangePasswordCommandDto() {
        super();
    }

    //

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

}
