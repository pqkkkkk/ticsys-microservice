package org.pqkkkkk.notification_service;

import org.pqkkkk.ticsys.grpc.notification.SendOTPVerificationCodeRequest;
import org.pqkkkk.ticsys.grpc.notification.SendOTPVerificationCodeResponse;

import io.grpc.stub.StreamObserver;

import org.pqkkkk.ticsys.grpc.notification.NotificationServiceGrpc.NotificationServiceImplBase;
import org.pqkkkkk.notification_service.entity.EmailDetails;
import org.pqkkkkk.notification_service.service.EmailService;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl extends NotificationServiceImplBase {
    private final EmailService emailService;

    public NotificationServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }
    @Override
    public void sendOTPVerificationCode(SendOTPVerificationCodeRequest request,
                                        StreamObserver<SendOTPVerificationCodeResponse> responseObserver) {
        String email = request.getEmail();
        String otpCode = request.getOtpCode();

        boolean isSent = emailService.sendSimpleMail(
                EmailDetails.builder()
                        .recipient(email)
                        .msgBody("Your OTP code is: " + otpCode)
                        .subject("OTP Verification Code for TicSys Account Registration")
                        .build()
        );

        // Here you would implement the logic to send the OTP code to the user's email.
        SendOTPVerificationCodeResponse response = SendOTPVerificationCodeResponse.newBuilder()
                .setSuccess(isSent)
                .setMessage(isSent ? "OTP code sent successfully to " + email : "Failed to send OTP code to " + email)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
