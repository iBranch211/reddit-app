package org.baeldung.web.dto.command;

import java.util.Set;

import org.baeldung.persistence.model.Role;
import org.baeldung.reddit.util.Dto;
import org.baeldung.web.dto.command.ICommandDto;

public class UserUpdateCommandDto implements Dto, ICommandDto {
    private Long id;

    private boolean enabled;

    private Set<Role> roles;

    //

    public UserUpdateCommandDto() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(final Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    //

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("UserCommandDto [id=").append(id).append(", enabled=").append(enabled).append(", roles=").append(roles).append("]");
        return builder.toString();
    }

}
