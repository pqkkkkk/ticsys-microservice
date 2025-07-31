package org.pqkkkkk.ticsys.identity_service.api;

import org.pqkkkkk.ticsys.identity_service.api.Response.ApiResponse;
import org.pqkkkkk.ticsys.identity_service.dto.BusinessResult.RefreshTokenResult;
import org.pqkkkkk.ticsys.identity_service.dto.BusinessResult.SignInResult;
import org.pqkkkkk.ticsys.identity_service.dto.DTO.UserDTO;
import org.pqkkkkk.ticsys.identity_service.dto.Request.GoogleAuthRequest;
import org.pqkkkkk.ticsys.identity_service.dto.Request.RefreshTokenRequest;
import org.pqkkkkk.ticsys.identity_service.dto.Request.SendOTPRequest;
import org.pqkkkkk.ticsys.identity_service.dto.Request.SignUpRequest;
import org.pqkkkkk.ticsys.identity_service.dto.Request.UsernamePasswordAuthRequest;
import org.pqkkkkk.ticsys.identity_service.dto.Request.VerifyOTPRequest;
import org.pqkkkkk.ticsys.identity_service.entity.User;
import org.pqkkkkk.ticsys.identity_service.service.AbstractAuthService;
import org.pqkkkkk.ticsys.identity_service.service.OtpService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthApi {
    private final OtpService otpService;
    private final AbstractAuthService<UsernamePasswordAuthRequest> usernamePasswordAuthService;
    private final AbstractAuthService<GoogleAuthRequest> googleAuthService;

    public AuthApi(@Qualifier("usernamePasswordAuthService") AbstractAuthService<UsernamePasswordAuthRequest> usernamePasswordAuthService,
                     @Qualifier("googleAuthService") AbstractAuthService<GoogleAuthRequest> googleAuthService,
                     OtpService otpService) {
        this.usernamePasswordAuthService = usernamePasswordAuthService;
        this.googleAuthService = googleAuthService;
        this.otpService = otpService;
    }

    @PostMapping("/signin/username-password")
    public ResponseEntity<ApiResponse<SignInResult>> signIn(@Valid @RequestBody UsernamePasswordAuthRequest request) {
        SignInResult result = usernamePasswordAuthService.signIn(request);
        ApiResponse<SignInResult> response = new ApiResponse<>(result, true, HttpStatus.OK.value(), "Sign in successful");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserDTO>> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        User user = SignUpRequest.toUser(signUpRequest);
        User createdUser = usernamePasswordAuthService.signUp(user);
        
        UserDTO userDTO = UserDTO.from(createdUser);
        ApiResponse<UserDTO> response = new ApiResponse<>(userDTO, true, HttpStatus.CREATED.value(), "User created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<RefreshTokenResult>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshTokenResult result = usernamePasswordAuthService.refreshToken(request.refreshToken());
        ApiResponse<RefreshTokenResult> response = new ApiResponse<>(result, true, HttpStatus.OK.value(), "Token refreshed successfully");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signin/with-google")
    public ResponseEntity<ApiResponse<SignInResult>> signInWithGoogle(@Valid @RequestBody GoogleAuthRequest request) {
        SignInResult result = googleAuthService.signIn(request);

        ApiResponse<SignInResult> response = new ApiResponse<>(result, true, HttpStatus.OK.value(), "Sign in successful");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/otp/send")
    public ResponseEntity<ApiResponse<Void>> sendOtp(@Valid @RequestBody SendOTPRequest request) {
        // Call the service to send OTP
        otpService.sendOTP(request.email());

        ApiResponse<Void> response = new ApiResponse<>(null, true, HttpStatus.OK.value(), "OTP sent successfully");
        return ResponseEntity.ok(response);
    }
    @PutMapping("/otp/verify")
    public ResponseEntity<ApiResponse<Void>> verifyOtp(@Valid @RequestBody VerifyOTPRequest request) {
        // Call the service to verify OTP
        otpService.verifyOTP(request.email(), request.otpCode());

        ApiResponse<Void> response = new ApiResponse<>(null, true, HttpStatus.OK.value(), "OTP verified successfully");
        return ResponseEntity.ok(response);
    }
}