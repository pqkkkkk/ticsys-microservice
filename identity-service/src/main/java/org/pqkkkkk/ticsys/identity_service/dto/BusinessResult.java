package org.pqkkkkk.ticsys.identity_service.dto;

import org.pqkkkkk.ticsys.identity_service.dto.DTO.UserDTO;

public class BusinessResult {
    public record SignInResult(
        String accessToken,
        String refreshToken,
        UserDTO user,
        boolean authenticated
    ){}
    public record RefreshTokenResult(
        String accessToken,
        String refreshToken
    ){}
    public record SignUpWithThirdPartyResult(
        String otpCode
    ){}
}
