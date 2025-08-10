package org.pqkkkkk.ticsys.event_service.client.identity;

import org.pqkkkk.ticsys.grpc.user.IsValidUserRequest;
import org.pqkkkk.ticsys.grpc.user.IsValidUserResponse;
import org.pqkkkk.ticsys.grpc.user.IdentityServiceGrpc.IdentityServiceBlockingStub;
import org.springframework.stereotype.Service;

import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class IdentityGrpcClient implements IdentityClient {
    @GrpcClient("identity-service")
    private IdentityServiceBlockingStub blockingStub;

    @Override
    public boolean isValidUser(Long userId) {
        IsValidUserRequest request = IsValidUserRequest.newBuilder()
                .setUserId(userId)
                .build();
        IsValidUserResponse response = blockingStub.isValidUser(request);
        return response.getIsValid();
    }
}
