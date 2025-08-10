package org.pqkkkkk.ticsys.identity_service.dao;

import org.pqkkkkk.ticsys.identity_service.dao.jpa_repository.UserRepository;
import org.pqkkkkk.ticsys.identity_service.entity.User;
import org.pqkkkkk.ticsys.identity_service.exception.UserNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class UserJpaDao implements UserDao {
    private final UserRepository userRepository;

    public UserJpaDao(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }
    @Override
    public User updateUser(User user) {
        userRepository.findById(user.getUserId())
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + user.getUserId()));

        return userRepository.save(user);
    }
    
    @Override
    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
