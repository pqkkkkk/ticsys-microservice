package org.pqkkkkk.identity_service.dto;

import org.pqkkkkk.identity_service.dto.DTO.UserDTO;

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
}
