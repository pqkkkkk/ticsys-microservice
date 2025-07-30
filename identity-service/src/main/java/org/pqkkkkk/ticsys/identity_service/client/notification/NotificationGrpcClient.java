package org.pqkkkkk.ticsys.identity_service.client.notification;

import org.pqkkkk.ticsys.grpc.notification.NotificationServiceGrpc.NotificationServiceBlockingStub;
import org.pqkkkk.ticsys.grpc.notification.SendOTPVerificationCodeRequest;
import org.pqkkkk.ticsys.grpc.notification.SendOTPVerificationCodeResponse;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;


@Service
@Slf4j
public class NotificationGrpcClient implements NotificationClient {

    @GrpcClient("notification-service")
    private  NotificationServiceBlockingStub notificationService;

    public NotificationGrpcClient() {
        
    }

    @Override
    public boolean sendOTPCodeVerification(String email, String otpCode) {
        log.info("Notification grpc server channel: {}", notificationService.getChannel());

        SendOTPVerificationCodeResponse response = notificationService.sendOTPVerificationCode(
                SendOTPVerificationCodeRequest.newBuilder()
                        .setEmail(email)
                        .setOtpCode(otpCode)
                        .build()
        );

        return response.getSuccess();
    }

}
