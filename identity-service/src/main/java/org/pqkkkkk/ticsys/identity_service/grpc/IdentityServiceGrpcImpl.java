package org.pqkkkkk.ticsys.identity_service.grpc;

import org.pqkkkk.ticsys.grpc.user.IsValidUserRequest;
import org.pqkkkk.ticsys.grpc.user.IsValidUserResponse;
import org.pqkkkk.ticsys.grpc.user.IdentityServiceGrpc.IdentityServiceImplBase;
import org.pqkkkkk.ticsys.identity_service.service.UserService;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@Slf4j
public class IdentityServiceGrpcImpl extends IdentityServiceImplBase {
    private UserService userService;

    public IdentityServiceGrpcImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void isValidUser(IsValidUserRequest request, StreamObserver<IsValidUserResponse> responseObserver) {
        log.info("Received request to check if user is valid: {}", request.getUserId());
        boolean isValid = userService.isValidUser(request.getUserId());

        IsValidUserResponse response = IsValidUserResponse.newBuilder()
                .setIsValid(isValid)
                .setMessage(isValid ? "User is valid" : "User is invalid")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
