package org.pqkkkkk.ticsys.identity_service.service.impl;


import org.pqkkkkk.ticsys.identity_service.Constants.UserStatus;
import org.pqkkkkk.ticsys.identity_service.dao.UserDao;
import org.pqkkkkk.ticsys.identity_service.entity.Role;
import org.pqkkkkk.ticsys.identity_service.entity.User;
import org.pqkkkkk.ticsys.identity_service.exception.UserNotFoundException;
import org.pqkkkkk.ticsys.identity_service.service.RoleService;
import org.pqkkkkk.ticsys.identity_service.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User addUser(User user) {
        assignDefaultRole(user);

        user.setPassWord(passwordEncoder.encode(user.getPassWord())); // encode password
        
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
        return userDao.getUserByUserName(userName);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
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

    @Override
    public boolean isValidUser(Long userId) {
        User user = userDao.getUserById(userId);

        if(user == null) {
            return false;
        }
        if(user.getStatus() != UserStatus.ACTIVE) {
            return false;
        }

        return true;
    }

}
