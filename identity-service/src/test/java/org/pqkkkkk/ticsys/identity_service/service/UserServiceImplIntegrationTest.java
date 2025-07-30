package org.pqkkkkk.ticsys.identity_service.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pqkkkkk.ticsys.identity_service.Constants.RoleEnum;
import org.pqkkkkk.ticsys.identity_service.dao.jpa_repository.RoleRepository;
import org.pqkkkkk.ticsys.identity_service.entity.Role;
import org.pqkkkkk.ticsys.identity_service.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.TransactionScoped;

@SpringBootTest
@ActiveProfiles("test")
@TransactionScoped
public class UserServiceImplIntegrationTest {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp() {
        // Clear the database before each test
        Role defaultRole = Role.builder()
                .name(RoleEnum.USER)
                .build();
        if(roleRepository.findByName(RoleEnum.USER) == null) {
            roleRepository.save(defaultRole);
        }
    }
    @Test
    public void isUserExists_WithExistingUsername_ShouldReturnTrue() {
        // Arrange
        User user = new User();
        user.setUserName("testUser");
        user.setEmail("testUser@example.com");
        user.setPassWord("password123");
        user.setFullName("Test User");
        userService.addUser(user);

        // Act
        boolean exists = userService.isUserExists(User.builder()
                .userName("testUser")
                .email("testUser@example.com")
                .build()
        );

        // Assert
        assertTrue(exists);
    }
    @Test
    public void isUserExists_WithExistingEmail_ShouldReturnTrue() {
        // Arrange
        User user = new User();
        user.setUserName("testUser2");
        user.setEmail("testUser2@example.com");
        user.setPassWord("password123");
        user.setFullName("Test User 2");
        userService.addUser(user);

        // Act
        boolean exists = userService.isUserExists(User.builder()
                .userName("testUser")
                .email("testUser2@example.com")
                .build()
        );

        // Assert
        assertTrue(exists);
    }
    @Test
    public void isUserExists_WithNonExistingUser_ShouldReturnFalse() {
        // Arrange
        User user = new User();
        user.setUserName("nonExistingUser");
        user.setEmail("nonExistingUser@example.com");

        // Act
        boolean exists = userService.isUserExists(user);

        // Assert
        assertFalse(exists);
    }
}