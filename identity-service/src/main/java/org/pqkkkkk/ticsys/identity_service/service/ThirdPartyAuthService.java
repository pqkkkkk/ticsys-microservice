package org.pqkkkkk.ticsys.identity_service.service;

import org.pqkkkkk.ticsys.identity_service.dto.BusinessResult.SignInResult;
import org.pqkkkkk.ticsys.identity_service.dto.BusinessResult.SignUpWithThirdPartyResult;
import org.pqkkkkk.ticsys.identity_service.entity.User;

public interface ThirdPartyAuthService {
    public SignInResult signIn(String accessToken);
    public SignUpWithThirdPartyResult HandleSignUpWithGoogleRequest(User user);
    public User HandleOTPRequest(String email, String otpCode);
}
