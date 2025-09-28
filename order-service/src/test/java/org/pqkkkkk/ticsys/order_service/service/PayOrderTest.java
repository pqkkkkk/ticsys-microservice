package org.pqkkkkk.ticsys.order_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.pqkkkkk.ticsys.order_service.domain.Constants.OrderStatus;
import org.pqkkkkk.ticsys.order_service.domain.dao.OrderCommandDao;
import org.pqkkkkk.ticsys.order_service.domain.entity.Order;
import org.pqkkkkk.ticsys.order_service.domain.entity.PayOrderCommand;
import org.pqkkkkk.ticsys.order_service.domain.usecase.NotificationService;
import org.pqkkkkk.ticsys.order_service.domain.usecase.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
public class PayOrderTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderCommandDao orderCommandDao;
    @MockitoBean
    @Qualifier("notificationMockClient")
    private NotificationService notificationService;

    @Test
    public void payOrder_WithInvalidOrderId_ShouldThrowException() {
        // Arrange
        Order invalidOrderInfo = Order.builder()
                .orderId(999L) // Assuming 999L is an invalid order ID
                .build();

        PayOrderCommand command = PayOrderCommand.builder()
            .order(invalidOrderInfo)
            .paymentMethod("CREDIT_CARD")
            .voucherCode("DISCOUNT10")
            .build();
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.payOrder(command));
    }

    @Test
    public void payOrder_WithValidOrder_ShouldChangeOrderStatusToPaid() {
        // Arrange
        Order preparedOrder = Order.builder()
                            .eventId(1L)
                            .userId(1L)
                            .subTotal(100.0)
                            .orderStatus(OrderStatus.PENDING)
                            .expiredAt(LocalDateTime.now().plusHours(1)) // 1 hour later
                            .build();
        orderCommandDao.addOrder(preparedOrder);

        Order validOrderInfo = Order.builder()
                .orderId(preparedOrder.getOrderId())
                .promotionId(1L)
                .build();
        
        PayOrderCommand command = PayOrderCommand.builder()
            .order(validOrderInfo)
            .paymentMethod("CREDIT_CARD")
            .voucherCode("DISCOUNT10")
            .build();

        // Act
        Order result = orderService.payOrder(command);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getTotal());
        assertEquals(OrderStatus.PAID, result.getOrderStatus());
        
        // Verify that notification service was called with the correct order
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(notificationService).sendOrderConfirmation(orderCaptor.capture());
        assertEquals(preparedOrder.getOrderId(), orderCaptor.getValue().getOrderId());
        assertEquals(OrderStatus.PAID, orderCaptor.getValue().getOrderStatus());
        assertNotNull(orderCaptor.getValue().getPromotionId());
    }

}
