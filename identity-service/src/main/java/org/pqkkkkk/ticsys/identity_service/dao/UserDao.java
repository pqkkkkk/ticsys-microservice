package org.pqkkkkk.ticsys.identity_service.dao;

import org.pqkkkkk.ticsys.identity_service.entity.User;

public interface UserDao {
    public User addUser(User user);
    public User updateUser(User user);
    public User getUserByUserName(String userName);
    public User getUserByEmail(String email);
}
