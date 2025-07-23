package org.pqkkkkk.identity_service.service;

import org.pqkkkkk.identity_service.Constants.RoleEnum;
import org.pqkkkkk.identity_service.entity.Role;

public interface RoleService {
    public Role getDefaultRole();
    public Role getRoleByName(RoleEnum name);
}
