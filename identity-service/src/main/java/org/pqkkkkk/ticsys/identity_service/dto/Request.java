package org.pqkkkkk.ticsys.identity_service.dto;

import java.util.Date;

import org.pqkkkkk.ticsys.identity_service.Constants.GenderEnum;
import org.pqkkkkk.ticsys.identity_service.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class Request {
    public record UsernamePasswordAuthRequest(
        @NotBlank
        String username,

        @NotBlank
        String password) {
    }
    public record GoogleAuthRequest(
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
    public record VerifyOTPRequest(
        @NotBlank
        String email,
        @NotBlank
        String otpCode
    ){}
    public record SendOTPRequest(
        @NotBlank
        String email
    ){}
}
