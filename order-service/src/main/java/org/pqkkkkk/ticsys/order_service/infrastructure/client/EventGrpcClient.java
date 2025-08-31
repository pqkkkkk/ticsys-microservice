package org.pqkkkkk.ticsys.order_service.infrastructure.client;

import java.util.List;

import org.pqkkkk.ticsys.grpc.event.ReverseTicketsAndCalculateSubtotalRequest;
import org.pqkkkk.ticsys.grpc.event.ValidateEventRequest;
import org.pqkkkk.ticsys.grpc.event.EventServiceGrpc.EventServiceBlockingStub;
import org.pqkkkkk.ticsys.order_service.domain.entity.TicketInOrder;
import org.pqkkkkk.ticsys.order_service.domain.usecase.EventService;
import org.springframework.stereotype.Service;

import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class EventGrpcClient implements EventService {
    @GrpcClient("event-service")
    private EventServiceBlockingStub blockingStub;

    @Override
    public boolean isValidEvent(Long eventId) {
        ValidateEventRequest request = ValidateEventRequest.newBuilder()
                .setEventId(eventId)
                .build();
        
        return blockingStub.validateEvent(request).getIsValid();
    }

    @Override
    public Double reverseTicketsAndCalculateSubtotal(Long eventId, Long eventDateId, List<TicketInOrder> ticketInOrders) {
        ReverseTicketsAndCalculateSubtotalRequest request = ReverseTicketsAndCalculateSubtotalRequest.newBuilder()
                .setEventId(eventId)
                .setEventDateId(eventDateId)
                .addAllTicketInOrders(ticketInOrders.stream()
                        .map(ticket -> org.pqkkkk.ticsys.grpc.event.TicketInOrder.newBuilder()
                                .setTicketId(ticket.getTicketId())
                                .setQuantity(ticket.getQuantity())
                                .build())
                        .toList())
                .build();

        return blockingStub.reverseTicketsAndCalculateSubtotal(request).getSubTotal();
    }

}
