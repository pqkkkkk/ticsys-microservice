package org.pqkkkkk.identity_service.api;

import java.util.Date;

import org.pqkkkkk.identity_service.Constants.GenderEnum;
import org.pqkkkkk.identity_service.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class Request {
    public record SignInRequest(
        @NotBlank
        String username,

        @NotBlank
        String password) {
    }
    public record SignInWithThirdPartyRequest(
        @NotBlank
        String idToken) {
    }

    public record RefreshTokenRequest(
        @NotBlank
        String refreshToken) {
    }
    public record SignUpRequest(
        @NotBlank
        String userName,
        @NotBlank
        String passWord,
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
        String email,
        @NotBlank
        String fullName,
        @NotBlank
        String phoneNumber,
        @NotNull
        Date birthday,
        @NotNull
        GenderEnum gender
    ){
        public static User toUser(SignUpRequest request){
            return User.builder()
                .userName(request.userName())
                .passWord(request.passWord())
                .email(request.email())
                .fullName(request.fullName())
                .phoneNumber(request.phoneNumber())
                .birthday(request.birthday())
                .gender(request.gender().name())
                .build();
        }
    }
    public record OTPRequest(
        @NotBlank
        String email,
        @NotBlank
        String otpCode
    ){}
}
