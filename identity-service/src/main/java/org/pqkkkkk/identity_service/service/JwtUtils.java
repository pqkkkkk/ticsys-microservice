package org.pqkkkkk.identity_service.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.pqkkkkk.identity_service.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Service
public class JwtUtils {
    @Value("${jwt.signer-key}")
    private  String SIGNER_KEY;
    @Value("${jwt.expiration}")
    private Long accessTokenExpiration;
    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    
    public String generateToken(User user, Long expiration)
    {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                                .subject(user.getUserName())
                                .issuer("pqkiet854")
                                .issueTime(new Date())
                                .expirationTime(new Date(
                                                Instant.now().plus(expiration, ChronoUnit.SECONDS).toEpochMilli()
                                                ))
                                .claim("scope", buildScope(user))
                                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateAcessToken(User user)
    {
        return generateToken(user, accessTokenExpiration);
    }
    public  String generateRefreshToken(User user){
        return generateToken(user, refreshTokenExpiration);
    }
    public  String extractUsername(String token){
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            return claimsSet.getSubject();
        } catch (ParseException e) {
            throw new RuntimeException("Invalid JWT token format", e);
        }
    }
    public boolean validateToken(String token){
        try {
            // Parse the JWT token
            SignedJWT signedJWT = SignedJWT.parse(token);
            
            // Verify the signature
            MACVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
            if (!signedJWT.verify(verifier)) {
                return false;
            }
            
            // Check if token is expired
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            Date expirationTime = claimsSet.getExpirationTime();
            
            if (expirationTime == null) {
                return false;
            }
            
            return new Date().before(expirationTime);
            
        } catch (ParseException | JOSEException e) {
            // Token parsing or verification failed
            return false;
        }
    }
    public  String buildScope(User user)
    {
        return "USER";
    }
}
