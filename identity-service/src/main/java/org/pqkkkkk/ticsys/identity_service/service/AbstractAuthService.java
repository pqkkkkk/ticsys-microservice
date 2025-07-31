package org.pqkkkkk.ticsys.identity_service.service;

import org.pqkkkkk.ticsys.identity_service.dto.BusinessResult.RefreshTokenResult;
import org.pqkkkkk.ticsys.identity_service.dto.BusinessResult.SignInResult;
import org.pqkkkkk.ticsys.identity_service.dto.DTO.UserDTO;
import org.pqkkkkk.ticsys.identity_service.entity.User;
import org.pqkkkkk.ticsys.identity_service.exception.ExistedUserException;
import org.pqkkkkk.ticsys.identity_service.utils.JwtUtils;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;

public abstract class AbstractAuthService<AuthRequest> {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    protected final String defaultPassword = "123456";

    public AbstractAuthService(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }
    public SignInResult signIn(AuthRequest request){
        User user = authenticate(request);

        return createSignInResult(user);
    }
    protected abstract User authenticate(AuthRequest request);

    private SignInResult createSignInResult(User user) {
        String accessToken = jwtUtils.generateAcessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);
        user.setPassWord(null); // Clear password before returning user object
        
        UserDTO userDto = UserDTO.from(user);

        return new SignInResult(accessToken, refreshToken, userDto, true);
    }

    public User signUp(User user) {
        if(userService.isUserExists(user)){
            throw new ExistedUserException("User already exists with username: " + user.getUserName() + " or email: " + user.getEmail());
        }
        
        User createdUser = userService.addUser(user);
        createdUser.setPassWord(null); // Clear password before returning user object   


        return createdUser;
    }

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
