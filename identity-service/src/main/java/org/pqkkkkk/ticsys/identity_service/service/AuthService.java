package org.pqkkkkk.ticsys.identity_service.service;

import org.pqkkkkk.ticsys.identity_service.dto.BusinessResult.RefreshTokenResult;
import org.pqkkkkk.ticsys.identity_service.dto.BusinessResult.SignInResult;
import org.pqkkkkk.ticsys.identity_service.entity.User;

public interface AuthService {
    public SignInResult signIn(String username, String password);
    public User signUp(User user);
    public RefreshTokenResult refreshToken(String refreshToken);
}
