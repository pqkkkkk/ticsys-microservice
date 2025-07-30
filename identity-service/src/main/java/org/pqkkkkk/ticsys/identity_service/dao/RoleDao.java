package org.pqkkkkk.ticsys.identity_service.dao;

import org.pqkkkkk.ticsys.identity_service.Constants.RoleEnum;
import org.pqkkkkk.ticsys.identity_service.entity.Role;

public interface RoleDao {
    public Role getRoleByName(RoleEnum name);
}
