package org.pqkkkkk.identity_service.service.impl;



import org.pqkkkkk.identity_service.dto.BusinessResult.RefreshTokenResult;
import org.pqkkkkk.identity_service.dto.BusinessResult.SignInResult;
import org.pqkkkkk.identity_service.dto.DTO.UserDTO;
import org.pqkkkkk.identity_service.entity.User;
import org.pqkkkkk.identity_service.exception.ExistedUserException;
import org.pqkkkkk.identity_service.exception.UserNotFoundException;
import org.pqkkkkk.identity_service.exception.WrongPasswordException;
import org.pqkkkkk.identity_service.service.AuthService;
import org.pqkkkkk.identity_service.service.JwtUtils;
import org.pqkkkkk.identity_service.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Value("${jwt.signer-key}")
    private String SIGNER_KEY;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public SignInResult signIn(String username, String password) {
        User user = userService.getUserByUserName(username);

        validatePassword(password, user.getPassWord());

        return createSignInResult(user);
    }
    private void validatePassword(String password, String encodedPassword) {
        boolean isValid = passwordEncoder.matches(password, encodedPassword);

        if (!isValid) {
            throw new WrongPasswordException("Invalid password format");
        }
    }
    private SignInResult createSignInResult(User user) {
        String accessToken = jwtUtils.generateAcessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);
        user.setPassWord(null); // Clear password before returning user object
        
        UserDTO userDto = UserDTO.from(user);

        return new SignInResult(accessToken, refreshToken, userDto, true);
    }

    @Override
    public User signUp(User user) {
        checkUserExists(user.getUserName());

        user.setPassWord(passwordEncoder.encode(user.getPassWord())); // encode password
        
        User createdUser = userService.addUser(user);

        return prepareCreatedUser(createdUser);
    }
    private void checkUserExists(String username){
        try{
            User existingUser = userService.getUserByUserName(username);

            if(existingUser != null){
                throw new ExistedUserException("User already exists with username: " + username);
            }
        } catch (UserNotFoundException e) {
            // User does not exist, continue with sign up
        }
    }
    private User prepareCreatedUser(User user) {
        return User.builder()
                .userName(user.getUserName())
                .passWord(null)
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .birthday(user.getBirthday())
                .gender(user.getGender())
                .roles(user.getRoles())
                .fullName(user.getFullName())
                .build();
    }
    
    @Override
    public RefreshTokenResult refreshToken(String refreshToken) {
        validateRefreshToken(refreshToken);

        String username = jwtUtils.extractUsername(refreshToken);
        User user = userService.getUserByUserName(username);

        String newAccessToken = jwtUtils.generateAcessToken(user);

        return new RefreshTokenResult(newAccessToken, refreshToken);
    }
    /**
     * Validates the provided refresh token.
     * @param refreshToken
     * @throws InvalidBearerTokenException if the refresh token is invalid.
     */
    private void validateRefreshToken(String refreshToken) {
        boolean isValid = jwtUtils.validateToken(refreshToken);
        if (!isValid) {
            throw new InvalidBearerTokenException("Invalid refresh token: " + refreshToken);
        }
    }
}
