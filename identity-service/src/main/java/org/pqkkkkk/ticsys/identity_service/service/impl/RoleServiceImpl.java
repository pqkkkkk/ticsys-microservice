package org.pqkkkkk.ticsys.identity_service.service.impl;

import org.pqkkkkk.ticsys.identity_service.Constants.RoleEnum;
import org.pqkkkkk.ticsys.identity_service.dao.RoleDao;
import org.pqkkkkk.ticsys.identity_service.entity.Role;
import org.pqkkkkk.ticsys.identity_service.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role getDefaultRole() {
        return roleDao.getRoleByName(RoleEnum.USER);
    }

    @Override
    public Role getRoleByName(RoleEnum name) {
        return roleDao.getRoleByName(name);
    }

}
