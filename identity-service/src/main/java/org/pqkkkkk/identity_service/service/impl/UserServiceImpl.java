package org.pqkkkkk.identity_service.service.impl;


import org.pqkkkkk.identity_service.dao.UserDao;
import org.pqkkkkk.identity_service.entity.User;
import org.pqkkkkk.identity_service.exception.UserNotFoundException;
import org.pqkkkkk.identity_service.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User addUser(User user) {
        return userDao.addUser(user);
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
