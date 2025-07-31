package org.pqkkkkk.ticsys.identity_service.service;

import org.pqkkkkk.ticsys.identity_service.exception.InvalidOTPException;

public interface OtpService {
        /**
     * Sends an OTP to the user's email.
     * @param email The email address to send the OTP to.
     */
    public abstract void sendOTP(String email);
    
    /**
     * Verifies the OTP sent to the user's email.
     * @param email The email address to verify the OTP for.
     * @param otpCode The OTP code to verify.
     * @throws InvalidOTPException if the OTP is invalid or expired.
     */
    public abstract void verifyOTP(String email, String otpCode);
}
