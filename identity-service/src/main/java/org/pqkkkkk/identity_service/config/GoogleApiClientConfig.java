package org.pqkkkkk.identity_service.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

@Configuration
public class GoogleApiClientConfig {
    private final String GOOGLE_CLIENT_ID = "20885154538-ui93v2f0gm354hskorfa1cdm5bvl9go4.apps.googleusercontent.com";

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
                HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                            .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                            .build();
        
        return verifier;
    }
}
