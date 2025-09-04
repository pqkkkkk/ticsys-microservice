package org.pqkkkkk.ticsys.order_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.pqkkkkk.ticsys.order_service.domain.Constants.OrderStatus;
import org.pqkkkkk.ticsys.order_service.domain.dao.OrderCommandDao;
import org.pqkkkkk.ticsys.order_service.domain.entity.Order;
import org.pqkkkkk.ticsys.order_service.domain.usecase.NotificationService;
import org.pqkkkkk.ticsys.order_service.domain.usecase.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class PayOrderTest {
    
    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public NotificationService notificationService() {
            NotificationService mockNotificationService = mock(NotificationService.class);
            when(mockNotificationService.sendOrderConfirmation(any(Order.class))).thenReturn(true);
            return mockNotificationService;
        }
    }
    
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderCommandDao orderCommandDao;
    @Autowired
    private NotificationService notificationService;

    @Test
    public void payOrder_WithInvalidOrderId_ShouldThrowException() {
        // Arrange
        Order invalidOrderInfo = Order.builder()
                .orderId(999L) // Assuming 999L is an invalid order ID
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.payOrder(invalidOrderInfo));
    }

    @Test
    public void payOrder_WithValidOrder_ShouldChangeOrderStatusToPaid() {
        // Arrange
        Order preparedOrder = Order.builder()
                            .eventId(1L)
                            .userId(1L)
                            .subTotal(100.0)
                            .orderStatus(OrderStatus.PENDING)
                            .build();
        orderCommandDao.addOrder(preparedOrder);
        Order validOrderInfo = Order.builder()
                .orderId(preparedOrder.getOrderId())
                .build();

        // Act
        Order result = orderService.payOrder(validOrderInfo);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getTotal());
        assertEquals(OrderStatus.PAID, result.getOrderStatus());
        
        // Verify that notification service was called with the correct order
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(notificationService).sendOrderConfirmation(orderCaptor.capture());
        assertEquals(preparedOrder.getOrderId(), orderCaptor.getValue().getOrderId());
    }

}
