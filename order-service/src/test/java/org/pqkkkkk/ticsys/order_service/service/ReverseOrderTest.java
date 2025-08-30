package org.pqkkkkk.ticsys.order_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.pqkkkkk.ticsys.order_service.domain.Constants.OrderStatus;
import org.pqkkkkk.ticsys.order_service.domain.entity.Order;
import org.pqkkkkk.ticsys.order_service.domain.usecase.EventService;
import org.pqkkkkk.ticsys.order_service.domain.usecase.IdentityService;
import org.pqkkkkk.ticsys.order_service.domain.usecase.NotificationService;
import org.pqkkkkk.ticsys.order_service.domain.usecase.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
public class ReverseOrderTest {
    @Autowired
    private OrderService orderService;
    @MockitoBean
    private IdentityService identityService;
    @MockitoBean
    private EventService eventService;

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

    @Test
    public void reverseOrder_WithInvalidUser_ShouldThrowException() {
        // Arrange
        Order order = Order.builder()
                .userId(1L)
                .build();
        when(identityService.isValidUser(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.reverseOrder(order));
    }
    @Test
    public void reverseOrder_WithValidInfo_ShouldReverseOrder(){
        // Arrange
        Order order = Order.builder()
                .userId(1L)
                .eventId(1L)
                .build();
        when(identityService.isValidUser(1L)).thenReturn(true);
        when(eventService.reverseTicketsAndCalculateSubtotal(1L, order.getTicketInOrders())).thenReturn(100.0);

        // Act
        Order reversedOrder = orderService.reverseOrder(order);

        // Assert
        assertNotNull(reversedOrder);
        assertNotNull(reversedOrder.getOrderId());
        assertEquals(100.0, reversedOrder.getSubTotal());
        assertEquals(OrderStatus.PENDING, reversedOrder.getOrderStatus());
    }
}
