package org.pqkkkkk.ticsys.identity_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pqkkkkk.ticsys.identity_service.Constants.RoleEnum;
import org.pqkkkkk.ticsys.identity_service.Constants.UserStatus;
import org.pqkkkkk.ticsys.identity_service.client.notification.NotificationClient;
import org.pqkkkkk.ticsys.identity_service.dao.jpa_repository.RoleRepository;
import org.pqkkkkk.ticsys.identity_service.dto.BusinessResult.SignUpWithThirdPartyResult;
import org.pqkkkkk.ticsys.identity_service.entity.Role;
import org.pqkkkkk.ticsys.identity_service.entity.User;
import org.pqkkkkk.ticsys.identity_service.exception.ExistedUserException;
import org.pqkkkkk.ticsys.identity_service.exception.InvalidOTPException;
import org.pqkkkkk.ticsys.identity_service.exception.UserNotFoundException;
import org.pqkkkkk.ticsys.identity_service.service.impl.GoogleAuthService;
import org.pqkkkkk.ticsys.identity_service.utils.OTPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class GoogleAuthServiceIntegrationTest {

    @Autowired
    private GoogleAuthService googleAuthService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private OTPUtils otpUtils;

    @MockitoBean
    private NotificationClient notificationClient;

    @BeforeEach
    public void setUp() {
        // Clear the database before each test
        Role defaultRole = Role.builder()
                .name(RoleEnum.USER)
                .build();

        if (roleRepository.findByName(RoleEnum.USER) == null) {
            roleRepository.save(defaultRole);
        }
    }
    // ---------------- Test cases for HandleSignUpWithGoogleRequest method ----------------
    @Test
    public void handleSignUpWithGoogleRequest_WithExistedUser_ShouldThrowExistedUserException() {
        // Arrange
        // Create a user that already exists in the database
        User existingUser = new User();
        existingUser.setUserName("existingUser");
        existingUser.setEmail("existingUser@example.com");
        existingUser.setPassWord("password123");
        existingUser.setFullName("Existing User");
        userService.addUser(existingUser);

        // Create a new user with the same email
        User newUser = new User();
        newUser.setUserName("newUser");
        newUser.setEmail("existingUser@example.com");

        // Act & expect an ExistedUserException to be thrown
        assertThrows(ExistedUserException.class, () -> {
            googleAuthService.HandleSignUpWithGoogleRequest(newUser);
        });
    }
    @Test
    public void handleSignUpWithGoogleRequest_WithNewUser_ShouldReturnOtpCode() {
        // Arrange
        User newUser = new User();
        newUser.setUserName("newUser");
        newUser.setEmail("newUser@example.com");
        newUser.setPassWord("password123");
        newUser.setFullName("New User");

        // Act
        // Mock the notification client to simulate sending an OTP code
        // Assuming the OTP code is generated and sent successfully
        when(notificationClient.sendOTPCodeVerification(anyString(), anyString())).thenReturn(true);

        SignUpWithThirdPartyResult result = googleAuthService.HandleSignUpWithGoogleRequest(newUser);

        // Assert
        assertNotNull(result);
        assertNotNull(result.otpCode());
    }

    // Test cases for HandleOTPRequest method
    @Test
    public void handleOTPRequest_WithValidOtp_ShouldActivateUser() {
        // Arrange
        User user = new User();
        user.setEmail("user@example.com");
        user.setUserName("user");
        user.setPassWord("password123");
        user.setFullName("Test User");
        user.setStatus(UserStatus.PENDING);
        userService.addUser(user);

        String otpCode = otpUtils.generateOTP(user.getEmail());
        // Act
        User result = googleAuthService.HandleOTPRequest(user.getEmail(), otpCode);

        // Assert
        assertNotNull(result);
        assertEquals(UserStatus.ACTIVE, result.getStatus());
    }
    @Test
    public void handleOTPRequest_WithNonExistentUser_ShouldThrowUserNotFoundException() {
        // Arrange
        String email = "nonExistentUser@example.com";
        String otpCode = otpUtils.generateOTP(email);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            googleAuthService.HandleOTPRequest(email, otpCode);
        });
    }
    @Test
    public void handleOTPRequest_WithNonExistOTPCode_ShouldThrowInvalidOTPException() {
        // Arrange
        User user = new User();
        user.setEmail("user@example.com");
        user.setUserName("user");
        user.setPassWord("password123");
        user.setFullName("Test User");
        user.setStatus(UserStatus.PENDING);
        userService.addUser(user);

        // Act & Assert
        assertThrows(InvalidOTPException.class, () -> {
            googleAuthService.HandleOTPRequest(user.getEmail(), "invalidOtpCode");
        });
    }
    @Test
    public void handleOTPRequest_WithValidData_ShouldReturnUser() {
        // Arrange
        User user = new User();
        user.setEmail("user@example.com");
        user.setUserName("user");
        user.setPassWord("password123");
        user.setFullName("Test User");
        user.setStatus(UserStatus.PENDING);
        userService.addUser(user);

        String otpCode = otpUtils.generateOTP(user.getEmail());
        // Act
        User result = googleAuthService.HandleOTPRequest(user.getEmail(), otpCode);

        // Assert
        assertNotNull(result);
        assertEquals(UserStatus.ACTIVE, result.getStatus());
    }
}