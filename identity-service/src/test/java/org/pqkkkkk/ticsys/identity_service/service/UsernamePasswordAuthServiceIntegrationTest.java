package org.pqkkkkk.ticsys.identity_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pqkkkkk.ticsys.identity_service.Constants.RoleEnum;
import org.pqkkkkk.ticsys.identity_service.dao.jpa_repository.RoleRepository;
import org.pqkkkkk.ticsys.identity_service.dto.BusinessResult.SignInResult;
import org.pqkkkkk.ticsys.identity_service.dto.Request.UsernamePasswordAuthRequest;
import org.pqkkkkk.ticsys.identity_service.entity.Role;
import org.pqkkkkk.ticsys.identity_service.entity.User;
import org.pqkkkkk.ticsys.identity_service.exception.ExistedUserException;
import org.pqkkkkk.ticsys.identity_service.exception.UserNotFoundException;
import org.pqkkkkk.ticsys.identity_service.exception.WrongPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UsernamePasswordAuthServiceIntegrationTest {
    @Autowired
    private AbstractAuthService<UsernamePasswordAuthRequest> usernamePasswordAuthService;
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

    // ------------------------- Test cases for signIn method ----------------
    @Test
    public void signIn_WithInvalidUsername_ShouldThrowUserNotFoundException() {
        // Given
        UsernamePasswordAuthRequest request = new UsernamePasswordAuthRequest("invalidUser", "password");

        // When & Then
        assertThrows(UserNotFoundException.class, () -> usernamePasswordAuthService.signIn(request));
    }
    @Test
    public void signIn_WithInvalidPassword_ShouldThrowWrongPasswordException() {
        // Given
        User existingUser = User.builder()
                .userName("existingUser")
                .passWord("encodedPassword")
                .email("existingUser@example.com")
                .fullName("Existing User")
                .build();
        userService.addUser(existingUser);
        UsernamePasswordAuthRequest request = new UsernamePasswordAuthRequest("existingUser", "wrongPassword");

        // When & Then
        assertThrows(WrongPasswordException.class, () -> usernamePasswordAuthService.signIn(request));
    }
    @Test
    public void signIn_WithValidCredentials_ShouldReturnSignInResult() {
        // Given
        User existingUser = User.builder()
                .userName("validUser")
                .passWord("encodedPassword")
                .email("validUser@example.com")
                .fullName("Valid User")
                .build();
        userService.addUser(existingUser);
        UsernamePasswordAuthRequest request = new UsernamePasswordAuthRequest("validUser", "encodedPassword");

        // When
        SignInResult result = usernamePasswordAuthService.signIn(request);

        // Then
        assertNotNull(result);
        assertEquals("validUser", result.user().userName());
    }

    // ------------------------- Test cases for signUp method ----------------
    @Test
    public void signUp_WithExistingUsername_ShouldThrowExistedUserException() {
        // Given
        User existingUser = User.builder()
                .userName("existingUser")
                .passWord("encodedPassword")
                .email("existingUser@example.com")
                .build();
        userService.addUser(existingUser);

        User newUser = User.builder()
                .userName("existingUser")
                .passWord("newPassword")
                .email("newUser@example.com")
                .build();
        
        // When & Then
        assertThrows(ExistedUserException.class, () -> usernamePasswordAuthService.signUp(newUser));
    }
    @Test
    public void signUp_WithExistingEmail_ShouldThrowExistedUserException() {
        // Given
        User existingUser = User.builder()
                .userName("existingUser")
                .passWord("encodedPassword")
                .email("existingUser@example.com")
                .build();
        userService.addUser(existingUser);

        User newUser = User.builder()
                .userName("newUser")
                .passWord("newPassword")
                .email("existingUser@example.com")
                .build();

        // When & Then
        assertThrows(ExistedUserException.class, () -> usernamePasswordAuthService.signUp(newUser));
    }
    @Test
    public void signUp_WithValidUser_ShouldReturnCreatedUser() {
        // Given
        User newUser = User.builder()
                .userName("newUser")
                .passWord("newPassword")
                .email("newUser@example.com")
                .build();
        
        // When
        User createdUser = usernamePasswordAuthService.signUp(newUser);

        assertNotNull(createdUser);
        assertEquals("newUser", createdUser.getUserName());
        assertEquals("newUser@example.com", createdUser.getEmail());
    }
}
