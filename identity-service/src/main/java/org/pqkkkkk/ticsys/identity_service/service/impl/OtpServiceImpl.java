package org.pqkkkkk.ticsys.identity_service.service.impl;

import org.pqkkkkk.ticsys.identity_service.client.notification.NotificationClient;
import org.pqkkkkk.ticsys.identity_service.exception.InvalidOTPException;
import org.pqkkkkk.ticsys.identity_service.service.OtpService;
import org.pqkkkkk.ticsys.identity_service.utils.OTPUtils;
import org.springframework.stereotype.Service;

@Service("otpService")
public class OtpServiceImpl implements OtpService {
    private final OTPUtils otpUtils;
    private final NotificationClient notificationClient;

    public OtpServiceImpl(OTPUtils otpUtils, NotificationClient notificationClient) {
        this.otpUtils = otpUtils;
        this.notificationClient = notificationClient;
    }

    @Override
    public void sendOTP(String email){
        String otpCode = otpUtils.generateOTP(email);

        notificationClient.sendOTPCodeVerification(email, otpCode);
    }
    @Override
    public void verifyOTP(String email, String otpCode) {
        if (!otpUtils.verifyOTP(email, otpCode)) {
            throw new InvalidOTPException("Invalid OTP code");
        }
    }

}
