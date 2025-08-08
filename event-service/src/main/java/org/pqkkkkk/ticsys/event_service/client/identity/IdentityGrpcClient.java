package org.pqkkkkk.ticsys.event_service.client.identity;

import org.springframework.stereotype.Service;

@Service
public class IdentityGrpcClient implements IdentityClient {

    @Override
    public boolean isValidUser(Long userId) {
        // Implement your gRPC call to the identity service here
        return false;
    }
}
