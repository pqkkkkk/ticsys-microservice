package org.pqkkkkk.ticsys.identity_service.service.impl;

import org.pqkkkkk.ticsys.identity_service.dto.Request.GoogleAuthRequest;
import org.pqkkkkk.ticsys.identity_service.entity.User;
import org.pqkkkkk.ticsys.identity_service.exception.InValidGoogleIdTokenException;
import org.pqkkkkk.ticsys.identity_service.service.AbstractAuthService;
import org.pqkkkkk.ticsys.identity_service.service.UserService;
import org.pqkkkkk.ticsys.identity_service.utils.GoogleUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

@Service("googleAuthService")
public class GoogleAuthServiceImpl extends AbstractAuthService<GoogleAuthRequest> {
    private final GoogleUtils googleUtils;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public GoogleAuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder,
                                 GoogleUtils googleUtils) {
        super(userService, null);
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.googleUtils = googleUtils;
    }
    @Override
    protected User authenticate(GoogleAuthRequest request) {
        String accessToken = request.idToken();
        GoogleIdToken idToken = googleUtils.verifyGoogleIdToken(accessToken);
        
        if (idToken == null) {  
            throw new InValidGoogleIdTokenException("Invalid Google ID token");
        }

        String email = idToken.getPayload().getEmail();
        User user = userService.getUserByEmail(email);

        // If user does not exist, create a new user with default password
        if(user == null){
            user = createDefaultUser(email, idToken.getPayload().get("name").toString());
        }

        return user;
    }
    private User createDefaultUser(String email, String name) {
        User user = new User();
        user.setEmail(email);
        user.setUserName(name);
        user.setPassWord(passwordEncoder.encode(defaultPassword));

        return userService.addUser(user);
    }
}
