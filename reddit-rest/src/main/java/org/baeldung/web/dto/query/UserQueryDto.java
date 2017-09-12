package org.baeldung.web.dto.query;

import java.util.Set;

import org.baeldung.persistence.model.Role;
import org.baeldung.reddit.util.Dto;

public class UserQueryDto implements Dto {
    private Long id;

    private String username;

    private boolean enabled;

    private Set<Role> roles;

    private long scheduledPostsCount;

    //

    public UserQueryDto() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(final Set<Role> roles) {
        this.roles = roles;
    }

    public long getScheduledPostsCount() {
        return scheduledPostsCount;
    }

    public void setScheduledPostsCount(final long scheduledPostsCount) {
        this.scheduledPostsCount = scheduledPostsCount;
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
        builder.append("UserDto [id=").append(id).append(", username=").append(username).append(", roles=").append(roles).append("]");
        return builder.toString();
    }

}
