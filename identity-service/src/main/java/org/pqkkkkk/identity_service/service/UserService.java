package org.pqkkkkk.identity_service.service;

import org.pqkkkkk.identity_service.entity.User;

public interface UserService {
    public User addUser(User user);

    /**
     * Fetches a user by their username.
     * Throws UserNotFoundException if the user is not found.
     * @param username
     * @return user
     * @throws UserNotFoundException
     */
    public User getUserByUserName(String username);
    public User getUserByEmail(String email);
}
