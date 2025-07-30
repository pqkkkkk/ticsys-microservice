package org.pqkkkkk.ticsys.identity_service.client.notification;

public interface NotificationClient {
    public boolean sendOTPCodeVerification(String email, String otpCode);
}
