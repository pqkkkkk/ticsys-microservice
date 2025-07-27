package org.pqkkkkk.identity_service.service.impl;

import org.pqkkkkk.identity_service.dto.BusinessResult.SignInResult;
import org.pqkkkkk.identity_service.dto.DTO.UserDTO;
import org.pqkkkkk.identity_service.entity.User;
import org.pqkkkkk.identity_service.exception.InValidGoogleIdTokenException;
import org.pqkkkkk.identity_service.service.ThirdPartyAuthService;
import org.pqkkkkk.identity_service.service.UserService;
import org.pqkkkkk.identity_service.utils.GoogleUtils;
import org.pqkkkkk.identity_service.utils.JwtUtils;
import org.pqkkkkk.identity_service.utils.OTPUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.openidconnect.IdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;


@Service
public class GoogleAuthService implements ThirdPartyAuthService {
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final GoogleUtils googleUtils;
    private final OTPUtils otpUtils;

    public GoogleAuthService(UserService userService, JwtUtils jwtUtils, OTPUtils otpUtils,
                             PasswordEncoder passwordEncoder, GoogleUtils googleUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.googleUtils = googleUtils;
        this.otpUtils = otpUtils;
    }

    @Override
    public SignInResult signIn(String accessToken) {
        GoogleIdToken idToken = googleUtils.verifyGoogleIdToken(accessToken);
        if (idToken == null) {
            throw new InValidGoogleIdTokenException("Invalid Google ID token");
        }

        String email = idToken.getPayload().getEmail();
        User user = userService.getUserByEmail(email);

        if(user == null){
            // If user does not exist, create a new user
            Payload payload = idToken.getPayload();
            user = new User();
            user.setEmail(email);
            user.setUserName(payload.get("name").toString());
            user.setPassWord(passwordEncoder.encode("temporaryPassword")); // Set a temporary password

            user = userService.addUser(user);
        }

        return createSignInResult(user);
    }
    private SignInResult createSignInResult(User user) {
        String accessToken = jwtUtils.generateAcessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);
        user.setPassWord(null); // Clear password before returning user object
        
        UserDTO userDto = UserDTO.from(user);

        return new SignInResult(accessToken, refreshToken, userDto, true);
    }

    @Override
    public void HandleSignUpWithGoogleRequest(User user) {
        // Create OTP code
        String otpCode = otpUtils.generateOTP(user.getEmail());

        // Save user to database with status as PENDING
        user.setPassWord(passwordEncoder.encode(otpCode)); // Store OTP as password temporarily
        userService.addUser(user);

        // Call to notification service to send OTP code
    }

    @Override
    public void HandleOTPRequest(String email, String otpCode) {
        // Validate OTP code
        boolean isValid = otpUtils.verifyOTP(email, otpCode);
        if(!isValid) {
            throw new RuntimeException("Invalid or expired OTP code"); // Custom exception can be used here
        }

        User correspondingUser = userService.getUserByEmail(email);
        if (correspondingUser == null) {
            throw new RuntimeException("User not found with email: " + email); // Custom exception can be used here
        }

        // Update user status to ACTIVE
        
    }

}
