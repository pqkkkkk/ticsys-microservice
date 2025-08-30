package org.pqkkkkk.ticsys.order_service.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.pqkkkkk.ticsys.order_service.domain.dao.OrderCommandDao;
import org.pqkkkkk.ticsys.order_service.domain.entity.Order;
import org.pqkkkkk.ticsys.order_service.domain.usecase.OrderQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class OrderQueryServiceTest {
    @Autowired
    private OrderQueryService orderQueryService;
    @Autowired
    private OrderCommandDao orderCommandDao;

    @Test
    public void isValidOrder_WithNonExistentOrderId_ReturnsFalse() {
        // Arrange
        Long nonExistentOrderId = 999L;

        // Act
        boolean result = orderQueryService.isValidOrder(nonExistentOrderId);

        // Assert
        assertFalse(result);
    }
    @Test
    public void isValidOrder_WithInvalidStatusOrderId_ReturnsFalse() {
        // Arrange
        Order order = Order.builder()
                .userId(1L)
                .eventId(1L)
                .orderStatus(org.pqkkkkk.ticsys.order_service.domain.Constants.OrderStatus.PAID)
                .build();
        orderCommandDao.addOrder(order);

        // Act
        boolean result = orderQueryService.isValidOrder(order.getOrderId());

        // Assert
        assertFalse(result);
    }
    @Test
    public void isValidOrder_WithValidStatusOrderId_ReturnsTrue() {
        // Arrange
        Order order = Order.builder()
                .userId(1L)
                .eventId(1L)
                .orderStatus(org.pqkkkkk.ticsys.order_service.domain.Constants.OrderStatus.PENDING)
                .build();
        orderCommandDao.addOrder(order);

        // Act
        boolean result = orderQueryService.isValidOrder(order.getOrderId());

        // Assert
        assertTrue(result);
    }
}
