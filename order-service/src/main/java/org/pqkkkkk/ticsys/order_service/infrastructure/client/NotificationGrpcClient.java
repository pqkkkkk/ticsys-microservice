package org.pqkkkkk.ticsys.order_service.infrastructure.client;

import org.pqkkkk.ticsys.grpc.notification.SendOrderConfirmationRequest;
import org.pqkkkk.ticsys.grpc.notification.TicketInOrder;
import org.pqkkkk.ticsys.grpc.notification.NotificationServiceGrpc.NotificationServiceBlockingStub;
import org.pqkkkkk.ticsys.order_service.domain.entity.Order;
import org.pqkkkkk.ticsys.order_service.domain.usecase.NotificationService;

import net.devh.boot.grpc.client.inject.GrpcClient;

public class NotificationGrpcClient implements NotificationService {

    @GrpcClient("notification-service")
    private NotificationServiceBlockingStub blockingStub;

    @Override
    public boolean sendOrderConfirmation(Order order) {
        return blockingStub.sendOrderConfirmation(buildRequest(order)).getSuccess();
    }

    private SendOrderConfirmationRequest buildRequest(Order order) {
        SendOrderConfirmationRequest request = SendOrderConfirmationRequest.newBuilder()
                .setOrderId(order.getOrderId())
                .setUserId(order.getUserId())
                .setEventId(order.getEventId())
                .setSubTotal(order.getSubTotal())
                .setPromotionId(order.getPromotionId())
                .addAllTicketInOrders(order.getTicketInOrders().stream()
                        .map(ticket -> TicketInOrder.newBuilder()
                                .setTicketTypeId(ticket.getTicketId())
                                .setQuantity(ticket.getQuantity())
                                .build())
                        .toList())
                .build();

        return request;
    }

}
