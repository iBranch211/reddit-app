package org.baeldung.service.impl.query;

import java.util.List;

import org.baeldung.persistence.dao.RoleRepository;
import org.baeldung.persistence.model.Role;
import org.baeldung.service.query.IRoleQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class RoleQueryService implements IRoleQueryService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> getRolesList() {
        return roleRepository.findAll();
    }

}
