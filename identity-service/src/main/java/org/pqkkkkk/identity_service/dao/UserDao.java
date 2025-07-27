package org.pqkkkkk.identity_service.dao;

import org.pqkkkkk.identity_service.entity.User;

public interface UserDao {
    public User addUser(User user);
    public User getUserByUserName(String userName);
    public User getUserByEmail(String email);
}
