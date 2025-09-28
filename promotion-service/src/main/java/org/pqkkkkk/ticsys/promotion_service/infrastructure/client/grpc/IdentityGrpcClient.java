package org.pqkkkkk.ticsys.promotion_service.infrastructure.client.grpc;

import org.pqkkkk.ticsys.grpc.user.IdentityServiceGrpc.IdentityServiceBlockingStub;
import org.pqkkkk.ticsys.grpc.user.IsValidUserRequest;
import org.pqkkkk.ticsys.grpc.user.IsValidUserResponse;
import org.pqkkkkk.ticsys.promotion_service.domain.service.external.IdentityService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
@Profile("!test")
public class IdentityGrpcClient implements IdentityService {
    @GrpcClient("identity-service")
    private IdentityServiceBlockingStub blockingStub;
    @Override
    public boolean isValidUser(Long userId) {
        try{
            IsValidUserRequest request = IsValidUserRequest.newBuilder()
                    .setUserId(userId)
                    .build();

            IsValidUserResponse response = blockingStub.isValidUser(request);
            
            return response.getIsValid();
        } catch (StatusRuntimeException exception) {
            return false;
        }
    }

}
