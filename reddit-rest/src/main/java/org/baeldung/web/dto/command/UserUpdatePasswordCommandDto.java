package org.baeldung.web.dto.command;

import org.baeldung.reddit.util.Dto;

public class UserUpdatePasswordCommandDto implements Dto, ICommandDto {

    private String oldPassword;
    private String password;

    public UserUpdatePasswordCommandDto() {
        super();
    }

    //

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(final String oldPassword) {
        this.oldPassword = oldPassword;
    }

}
