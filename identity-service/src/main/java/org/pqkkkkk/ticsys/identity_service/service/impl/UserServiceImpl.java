package org.pqkkkkk.ticsys.identity_service.service.impl;


import org.pqkkkkk.ticsys.identity_service.dao.UserDao;
import org.pqkkkkk.ticsys.identity_service.entity.Role;
import org.pqkkkkk.ticsys.identity_service.entity.User;
import org.pqkkkkk.ticsys.identity_service.exception.UserNotFoundException;
import org.pqkkkkk.ticsys.identity_service.service.RoleService;
import org.pqkkkkk.ticsys.identity_service.service.UserService;
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
    @Override
    public User updateUser(User user) {

        if(userDao.getUserByUserName(user.getUserName()) == null) {
            throw new UserNotFoundException("User not found with username: " + user.getUserName());
        }
        
        return userDao.updateUser(user);
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

    @Override
    public User getUserByEmail(String email) {
        User user = userDao.getUserByEmail(email);

        if(user == null){
            throw new UserNotFoundException("User not found with email: " + email);
        }
        
        return user;
    }
    @Override
    public boolean isUserExists(User user) {
        User existingUserWithUsername = userDao.getUserByUserName(user.getUserName());
        if(existingUserWithUsername != null){
            return true;
        }

        User existingUserWithEmail = userDao.getUserByEmail(user.getEmail());
        if(existingUserWithEmail != null){
            return true;
        }

        return false;
    }

}
