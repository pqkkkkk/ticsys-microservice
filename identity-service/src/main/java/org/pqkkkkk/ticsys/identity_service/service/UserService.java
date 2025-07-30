package org.pqkkkkk.ticsys.identity_service.service;

import org.pqkkkkk.ticsys.identity_service.entity.User;

public interface UserService {
    public User addUser(User user);
    public User updateUser(User user);

    /**
     * Fetches a user by their username.
     * Throws UserNotFoundException if the user is not found.
     * @param username
     * @return user
     * @throws UserNotFoundException
     */
    public User getUserByUserName(String username);
    public User getUserByEmail(String email);
    public boolean isUserExists(User user);
}
