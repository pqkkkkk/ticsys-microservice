package com.example.ticsys.app.order.dto.request;

import java.util.List;

import com.example.ticsys.app.order.model.Order;
import com.example.ticsys.app.order.model.TicketOfOrder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateOrderRequest {
    Order order;
    List<TicketOfOrder> ticketOfOrders;
}
