package org.pqkkkkk.ticsys.order_service.controller.http;

import org.pqkkkkk.ticsys.order_service.controller.http.dto.DTO.OrderDTO;
import org.pqkkkkk.ticsys.order_service.controller.http.dto.Request.CancelOrderRequest;
import org.pqkkkkk.ticsys.order_service.controller.http.dto.Request.PayOrderRequest;
import org.pqkkkkk.ticsys.order_service.controller.http.dto.Request.ReserveOrderRequest;
import org.pqkkkkk.ticsys.order_service.controller.http.dto.Response.ApiResponse;
import org.pqkkkkk.ticsys.order_service.domain.entity.Order;
import org.pqkkkkk.ticsys.order_service.domain.usecase.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RequestMapping("/api/v1/order")
@RestController
public class OrderApi {
    private final OrderService orderService;

    public OrderApi(OrderService orderService) {
        this.orderService = orderService;
    }
    @PostMapping("/reservation")
    public ResponseEntity<ApiResponse<OrderDTO>> reserveOrder(@Valid @RequestBody ReserveOrderRequest request) {
        Order order = request.toEntity();

        orderService.reserveOrder(order);

        OrderDTO dto = OrderDTO.fromEntity(order);

        ApiResponse<OrderDTO> response = new ApiResponse<>(dto, true, HttpStatus.CREATED.value(),
                            "Order created successfully", null);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("/payment")
    public ResponseEntity<ApiResponse<OrderDTO>> payOrder(@Valid @RequestBody PayOrderRequest request) {
        Order exinstentOrderInfo = request.toEntity();

        Order order = orderService.payOrder(exinstentOrderInfo);

        OrderDTO dto = OrderDTO.fromEntity(order);

        ApiResponse<OrderDTO> response = new ApiResponse<>(dto, true, HttpStatus.OK.value(),
                            "Order payment successful", null);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PutMapping("/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@Valid @RequestBody CancelOrderRequest request) {
        // TODO: Implement cancel order logic
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
