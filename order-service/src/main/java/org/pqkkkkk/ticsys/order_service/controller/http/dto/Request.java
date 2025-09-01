package org.pqkkkkk.ticsys.order_service.controller.http.dto;

import org.pqkkkkk.ticsys.order_service.domain.entity.Order;

public class Request {
    public record ReserveOrderRequest(
        Long userId,
        Long eventId,
        Integer ticketCount
    ){
        public Order toEntity() {
            Order order = new Order();

            return order;
        }
    }
    public record PayOrderRequest(
        Long orderId,
        String paymentMethod
    ){
        public Order toEntity() {
            Order order = new Order();

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
            // Set other fields as needed
            return order;
        }
    }
}
