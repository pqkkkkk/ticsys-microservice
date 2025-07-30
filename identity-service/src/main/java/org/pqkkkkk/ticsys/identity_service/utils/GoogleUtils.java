package org.pqkkkkk.ticsys.identity_service.utils;

import org.pqkkkkk.ticsys.identity_service.exception.InValidGoogleIdTokenException;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

@Service
public class GoogleUtils {
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    public GoogleUtils(GoogleIdTokenVerifier googleIdTokenVerifier) {
        this.googleIdTokenVerifier = googleIdTokenVerifier;
    }

    public  GoogleIdToken verifyGoogleIdToken(String accessToken) {
        try {
            return googleIdTokenVerifier.verify(accessToken);
        } catch (Exception e) {
            throw new InValidGoogleIdTokenException(e.getMessage());
        }
    }
}
