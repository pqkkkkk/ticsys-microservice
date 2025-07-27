package org.pqkkkkk.identity_service.service;

import org.pqkkkkk.identity_service.dto.BusinessResult.SignInResult;
import org.pqkkkkk.identity_service.entity.User;

public interface ThirdPartyAuthService {
    public SignInResult signIn(String accessToken);
    public void HandleSignUpWithGoogleRequest(User user);
    public void HandleOTPRequest(String email, String otpCode);
}
