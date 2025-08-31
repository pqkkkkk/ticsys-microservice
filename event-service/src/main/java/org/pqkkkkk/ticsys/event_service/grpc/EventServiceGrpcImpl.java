package org.pqkkkkk.ticsys.event_service.grpc;

import org.pqkkkk.ticsys.grpc.event.ReverseTicketsAndCalculateSubtotalRequest;
import org.pqkkkk.ticsys.grpc.event.ReverseTicketsAndCalculateSubtotalResponse;
import org.pqkkkk.ticsys.grpc.event.EventServiceGrpc.EventServiceImplBase;
import org.pqkkkkk.ticsys.common_domain_contracts.event.TicketQuantity;
import org.pqkkkkk.ticsys.common_domain_contracts.event.TicketReservation;
import org.pqkkkkk.ticsys.event_service.service.EventInventoryService;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@Slf4j
public class EventServiceGrpcImpl extends EventServiceImplBase {
    private final EventInventoryService eventInventoryService;

    public EventServiceGrpcImpl(EventInventoryService eventInventoryService) {
        this.eventInventoryService = eventInventoryService;
    }

    @Override
    public void reverseTicketsAndCalculateSubtotal(ReverseTicketsAndCalculateSubtotalRequest request, 
                    StreamObserver<ReverseTicketsAndCalculateSubtotalResponse> responseObserver) {
        TicketReservation ticketReservation = buildTicketReservation(request);

        Double subTotal = eventInventoryService.reserveTickets(ticketReservation);

        responseObserver.onNext(ReverseTicketsAndCalculateSubtotalResponse.newBuilder()
                .setSubTotal(subTotal)
                .setIsValidEvent(subTotal != null)
                .setMessage(subTotal != null ? "Tickets reversed successfully." : "Failed to reverse tickets.")
                .build());
        responseObserver.onCompleted();
    }

    private TicketReservation buildTicketReservation(ReverseTicketsAndCalculateSubtotalRequest request) {
        return TicketReservation.builder()
                .eventId(request.getEventId())
                .eventDateId(request.getEventDateId())
                .ticketQuantities(request.getTicketInOrdersList().stream()
                                .map(ticketInOrder -> TicketQuantity.builder()
                                        .ticketId(ticketInOrder.getTicketId())
                                        .quantity(ticketInOrder.getQuantity())
                                        .build())
                                .toList())
                .build();
    }
}
