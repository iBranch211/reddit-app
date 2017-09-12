package org.baeldung.web.dto.command;

import org.baeldung.reddit.util.Dto;

public class UserRegisterCommandDto implements Dto, ICommandDto {

    private String username;
    private String email;
    private String password;

    public UserRegisterCommandDto() {
        super();
    }

    //

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

}
