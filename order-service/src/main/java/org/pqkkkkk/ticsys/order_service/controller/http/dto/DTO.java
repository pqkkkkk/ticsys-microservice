package org.pqkkkkk.ticsys.order_service.controller.http.dto;

import org.pqkkkkk.ticsys.order_service.domain.entity.Order;
import org.pqkkkkk.ticsys.order_service.domain.entity.TicketInOrder;
import org.pqkkkkk.ticsys.order_service.domain.Constants.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DTO {

    /**
     * DTO record for Order entity
     */
    public record OrderDTO(
            Long orderId,
            Double subTotal,
            Double total,
            Long userId,
            Long eventDateId,
            Long eventId,
            Long promotionId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime expiredAt,
            OrderStatus orderStatus,
            List<TicketInOrderDTO> ticketInOrders
    ) {
        /**
         * Create a DTO from Order entity
         * @param entity Order entity
         * @return OrderDTO
         */
        public static OrderDTO fromEntity(Order entity) {
            List<TicketInOrderDTO> ticketDTOs = entity.getTicketInOrders() != null ?
                entity.getTicketInOrders().stream()
                    .map(TicketInOrderDTO::fromEntity)
                    .collect(Collectors.toList()) :
                List.of();

            return new OrderDTO(
                    entity.getOrderId(),
                    entity.getSubTotal(),
                    entity.getTotal(),
                    entity.getUserId(),
                    entity.getEventDateId(),
                    entity.getEventId(),
                    entity.getPromotionId(),
                    entity.getCreatedAt(),
                    entity.getUpdatedAt(),
                    entity.getExpiredAt(),
                    entity.getOrderStatus(),
                    ticketDTOs
            );
        }
    }

    /**
     * DTO record for TicketInOrder entity
     */
    public record TicketInOrderDTO(
            Long ticketInOrderId,
            Long ticketId,
            Integer quantity,
            Long orderId
    ) {
        /**
         * Create a DTO from TicketInOrder entity
         * @param entity TicketInOrder entity
         * @return TicketInOrderDTO
         */
        public static TicketInOrderDTO fromEntity(TicketInOrder entity) {
            return new TicketInOrderDTO(
                    entity.getTicketInOrderId(),
                    entity.getTicketId(),
                    entity.getQuantity(),
                    entity.getOrder() != null ? entity.getOrder().getOrderId() : null
            );
        }
    }
}
