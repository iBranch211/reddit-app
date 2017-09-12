package org.baeldung.service.query;

import java.util.List;

import org.baeldung.persistence.model.Role;

public interface IRoleQueryService {

    List<Role> getRolesList();

}
