package org.pqkkkkk.identity_service.dao;

import org.pqkkkkk.identity_service.Constants.RoleEnum;
import org.pqkkkkk.identity_service.entity.Role;

public interface RoleDao {
    public Role getRoleByName(RoleEnum name);
}
