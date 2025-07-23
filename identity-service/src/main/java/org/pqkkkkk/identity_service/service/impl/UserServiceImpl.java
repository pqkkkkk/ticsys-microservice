package org.pqkkkkk.identity_service.service.impl;


import org.pqkkkkk.identity_service.dao.UserDao;
import org.pqkkkkk.identity_service.entity.Role;
import org.pqkkkkk.identity_service.entity.User;
import org.pqkkkkk.identity_service.exception.UserNotFoundException;
import org.pqkkkkk.identity_service.service.RoleService;
import org.pqkkkkk.identity_service.service.UserService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final RoleService roleService;

    public UserServiceImpl(UserDao userDao, RoleService roleService) {
        this.userDao = userDao;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public User addUser(User user) {
        assignDefaultRole(user);

        return userDao.addUser(user);
    }
    private void assignDefaultRole(User user) {
        Role defaultRole = roleService.getDefaultRole();
        if (defaultRole == null) {
            throw new IllegalStateException("Default role not found");
        }

        user.getRoles().add(defaultRole);
    }

    @Override
    public User getUserByUserName(String userName) {
        User user = userDao.getUserByUserName(userName);

        if(user == null){
            throw new UserNotFoundException("User not found with username: " + userName);
        }
        
        return user;
    }

}
