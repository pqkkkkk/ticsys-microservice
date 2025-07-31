package org.pqkkkkk.ticsys.identity_service.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.pqkkkkk.ticsys.identity_service.dto.Request.GoogleAuthRequest;
import org.pqkkkkk.ticsys.identity_service.exception.InValidGoogleIdTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class GoogleAuthServiceIntegrationTest {
    @Autowired
    private AbstractAuthService<GoogleAuthRequest> googleAuthService;

    @Test
    public void signIn_WithInvalidGoogleIdToken_ShouldThrowInValidGoogleIdTokenException() {
        // Given
        GoogleAuthRequest request = new GoogleAuthRequest("invalidToken");

        // When & Then
        assertThrows(InValidGoogleIdTokenException.class, () -> googleAuthService.signIn(request));
    }
}
