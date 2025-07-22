package org.pqkkkkk.identity_service.api;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.pqkkkkk.identity_service.api.Request.RefreshTokenRequest;
import org.pqkkkkk.identity_service.api.Request.SignInRequest;
import org.pqkkkkk.identity_service.api.Request.SignUpRequest;
import org.pqkkkkk.identity_service.api.Response.ApiResponse;
import org.pqkkkkk.identity_service.dto.BusinessResult.RefreshTokenResult;
import org.pqkkkkk.identity_service.dto.BusinessResult.SignInResult;
import org.pqkkkkk.identity_service.dto.DTO.UserDTO;
import org.pqkkkkk.identity_service.entity.User;
import org.pqkkkkk.identity_service.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/v1/auth")
public class AuthApi {
    private final AuthService authService;

    public AuthApi(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<SignInResult>> login(@Valid @RequestBody SignInRequest request) {
        SignInResult result = authService.signIn(request.username(), request.password());
        ApiResponse<SignInResult> response = new ApiResponse<>(result, true, HttpStatus.OK.value(), "Sign in successful");
        return ResponseEntity.ok(response);
    }
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserDTO>> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        User user = SignUpRequest.toUser(signUpRequest);
        User createdUser = authService.signUp(user);
        
        UserDTO userDTO = UserDTO.from(createdUser);
        ApiResponse<UserDTO> response = new ApiResponse<>(userDTO, true, HttpStatus.CREATED.value(), "User created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<RefreshTokenResult>> refreshToken(@RequestBody RefreshTokenRequest request) {
        RefreshTokenResult result = authService.refreshToken(request.refreshToken());
        ApiResponse<RefreshTokenResult> response = new ApiResponse<>(result, true, HttpStatus.OK.value(), "Token refreshed successfully");
        
        return ResponseEntity.ok(response);
    }
    
}
