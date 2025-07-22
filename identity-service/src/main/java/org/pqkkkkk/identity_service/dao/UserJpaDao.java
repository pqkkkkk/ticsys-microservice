package org.pqkkkkk.identity_service.dao;

import org.pqkkkkk.identity_service.dao.jpa_repository.UserRepository;
import org.pqkkkkk.identity_service.entity.User;
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
    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
