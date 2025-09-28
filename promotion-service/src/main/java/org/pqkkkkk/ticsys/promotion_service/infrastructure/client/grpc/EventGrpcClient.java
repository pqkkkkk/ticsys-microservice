package org.pqkkkkk.ticsys.promotion_service.infrastructure.client.grpc;

import org.pqkkkk.ticsys.grpc.event.ValidateEventDateRequest;
import org.pqkkkk.ticsys.grpc.event.EventServiceGrpc.EventServiceBlockingStub;
import org.pqkkkkk.ticsys.promotion_service.domain.service.external.EventService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
@Profile("!test")
public class EventGrpcClient implements EventService {
    @GrpcClient("event-service")
    private EventServiceBlockingStub blockingStub;
    
    @Override
    public boolean isValidEventDate(Long eventId, Long eventDateId) {
        ValidateEventDateRequest request = ValidateEventDateRequest.newBuilder()
                .setEventId(eventId)
                .setEventDateId(eventDateId)
                .build();
        
        return blockingStub.validateEventDate(request).getIsValid();
    }

}
