package org.baeldung.web.controller.rest.query;

import java.util.List;

import org.baeldung.persistence.model.Role;
import org.baeldung.service.query.IRoleQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/users/roles")
public class RoleQueryController {

    @Autowired
    private IRoleQueryService roleService;

    @PreAuthorize("hasAuthority('USER_READ_PRIVILEGE')")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Role> getRolesList() {
        return roleService.getRolesList();
    }
}
