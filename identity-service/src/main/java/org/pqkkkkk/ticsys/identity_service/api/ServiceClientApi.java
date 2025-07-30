package org.pqkkkkk.ticsys.identity_service.api;

import org.pqkkkkk.ticsys.identity_service.FetchNotificationServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/service-client")
public class ServiceClientApi {
    @Autowired
    private FetchNotificationServiceTest fetchNotificationServiceTest;

    @RequestMapping("/notification-service/grpc-channel")
    public ResponseEntity<String> getNotificationServiceGrpcChannel() {
        String channel = fetchNotificationServiceTest.grpcChannel();
        return ResponseEntity.ok(channel);
    }
}
