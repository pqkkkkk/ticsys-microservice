package org.pqkkkkk.ticsys.order_service.controller.http.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.pqkkkkk.ticsys.order_service.domain.entity.Order;
import org.pqkkkkk.ticsys.order_service.domain.entity.TicketInOrder;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class Request {
    public record ReserveOrderRequest(
        @NotNull(message = "Invalid user id")
        @Min(value = 1, message = "Invalid user id")
        Long userId,

        @NotNull(message = "Invalid event id")
        @Min(value = 1, message = "Invalid event id")
        Long eventId,

        @NotNull(message = "Invalid event date id")
        @Min(value = 1, message = "Invalid event date id")
        Long eventDateId,

        @NotNull(message = "Invalid sub total")
        @Min(value = 0, message = "Invalid sub total")
        Double subTotal,

        @NotNull(message = "Invalid ticket list")
        @NotEmpty(message = "Invalid ticket list")
        List<ReserveTicketInOrderRequest> tickets
    ){
        public Order toEntity() {
            Order order = new Order();
            order.setUserId(this.userId);
            order.setEventId(this.eventId);
            order.setEventDateId(this.eventDateId);
            order.setSubTotal(this.subTotal);
            order.setTicketInOrders(this.tickets.stream().map(ReserveTicketInOrderRequest::toEntity).collect(Collectors.toList()));

            for(TicketInOrder tio : order.getTicketInOrders()) {
                tio.setOrder(order);
            }
            
            return order;
        }
    }
    public record ReserveTicketInOrderRequest(
        @NotNull(message = "Invalid ticket id")
        @Min(value = 1, message = "Invalid ticket id")
        Long ticketId,
        @NotNull(message = "Invalid quantity")
        @Min(value = 1, message = "Invalid quantity")
        Integer quantity
    ) {
        public TicketInOrder toEntity() {
            TicketInOrder ticketInOrder = new TicketInOrder();
            ticketInOrder.setTicketId(this.ticketId);
            ticketInOrder.setQuantity(this.quantity);
            return ticketInOrder;
        }
    }
    public record PayOrderRequest(
        @NotNull(message = "Invalid order id")
        Long orderId,
        String paymentMethod
    ){
        public Order toEntity() {
            Order order = new Order();
            order.setOrderId(this.orderId);

            return order;
        }
    }
    public record CancelOrderRequest(
        Long orderId,
        String reason
    ){
        public Order toEntity() {
            Order order = new Order();
            order.setOrderId(this.orderId);
            
            return order;
        }
    }
}
