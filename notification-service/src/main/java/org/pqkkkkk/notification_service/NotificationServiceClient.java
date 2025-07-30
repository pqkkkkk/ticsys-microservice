package org.pqkkkkk.notification_service;

import org.pqkkkk.ticsys.grpc.notification.NotificationServiceGrpc;
import org.pqkkkk.ticsys.grpc.notification.SendOTPVerificationCodeRequest;
import org.pqkkkk.ticsys.grpc.notification.SendOTPVerificationCodeResponse;
import org.pqkkkk.ticsys.grpc.notification.NotificationServiceGrpc.NotificationServiceBlockingStub;

import io.grpc.ManagedChannel;

public class NotificationServiceClient {
    public static void main(String[] args){
        ManagedChannel channel = io.grpc.ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        NotificationServiceBlockingStub stub = NotificationServiceGrpc.newBlockingStub(channel);

        SendOTPVerificationCodeRequest request = SendOTPVerificationCodeRequest.newBuilder()
                .setEmail("crkeo169@gmail.com")
                .setOtpCode("123456")
                .build();
        
        SendOTPVerificationCodeResponse response = stub.sendOTPVerificationCode(request);

        System.out.println("Response: " + response.getMessage());

        // Shutdown the channel
        channel.shutdown();
    }
}
