package org.pqkkkkk.ticsys.identity_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;


@Component
public class FetchNotificationServiceTest {
    @Autowired
    private EurekaClient eurekaClient;

    public String grpcChannel() {
    // lấy thông tin instance từ Eureka
    InstanceInfo instance = eurekaClient
        .getNextServerFromEureka("notification-service", false);
    String host = instance.getIPAddr();
    int grpcPort = Integer.parseInt(
        instance.getMetadata().get("grpc.port")
    );

    return String.format("%s:%d", host, grpcPort);
    }
}
