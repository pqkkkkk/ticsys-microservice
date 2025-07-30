package org.pqkkkkk.ticsys.identity_service.utils;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OTPUtils {
    private static final SecureRandom random = new SecureRandom();
    @Value("${otp.length}")
    private int OTP_LENGTH = 6; // Default OTP length
    @Value("${otp.expiry_minutes}")
    private int OTP_EXPIRY_MINUTES = 5;

    // Store OTP with expiry time
    private final ConcurrentHashMap<String, OTPData> otpStore = new ConcurrentHashMap<>();
    
    public String generateOTP(String email) {
        String otp = generateRandomOTP();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);
        
        otpStore.put(email, new OTPData(otp, expiryTime));
        
        return otp;
    }
    
    public boolean verifyOTP(String email, String inputOTP) {
        OTPData otpData = otpStore.get(email);
        
        if (otpData == null) {
            return false;
        }
        
        if (LocalDateTime.now().isAfter(otpData.getExpiryTime())) {
            otpStore.remove(email); // Remove expired OTP
            return false;
        }
        
        if (otpData.getOtp().equals(inputOTP)) {
            otpStore.remove(email); // Remove used OTP
            return true;
        }
        
        return false;
    }
    
    private String generateRandomOTP() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
    
    // Inner class to store OTP data
    private static class OTPData {
        private final String otp;
        private final LocalDateTime expiryTime;
        
        public OTPData(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }
        
        public String getOtp() { return otp; }
        public LocalDateTime getExpiryTime() { return expiryTime; }
    }
}