package org.pqkkkkk.ticsys.order_service.domain.usecase.impl;

import java.time.LocalDateTime;

import org.pqkkkkk.ticsys.order_service.domain.Constants.OrderStatus;
import org.pqkkkkk.ticsys.order_service.domain.Constants;
import org.pqkkkkk.ticsys.order_service.domain.dao.OrderCommandDao;
import org.pqkkkkk.ticsys.order_service.domain.entity.Order;
import org.pqkkkkk.ticsys.order_service.domain.usecase.EventService;
import org.pqkkkkk.ticsys.order_service.domain.usecase.NotificationService;
import org.pqkkkkk.ticsys.order_service.domain.usecase.OrderQueryService;
import org.pqkkkkk.ticsys.order_service.domain.usecase.OrderService;
import org.pqkkkkk.ticsys.order_service.domain.usecase.PaymentService;
import org.pqkkkkk.ticsys.order_service.domain.usecase.PromotionService;
import org.pqkkkkk.ticsys.order_service.domain.usecase.IdentityService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private OrderCommandDao orderCommandDao;
    private OrderQueryService orderQueryService;
    private EventService eventService;
    private PaymentService paymentService;
    private PromotionService promotionService;
    private IdentityService identityService;
    private NotificationService notificationService;

    public OrderServiceImpl(OrderCommandDao orderCommandDao,
                        OrderQueryService orderQueryService, EventService eventService, PaymentService paymentService,
                        PromotionService promotionService, IdentityService identityService,
                        @Qualifier("notificationMockClient") NotificationService notificationService) {
        this.orderCommandDao = orderCommandDao;
        this.eventService = eventService;
        this.paymentService = paymentService;
        this.promotionService = promotionService;
        this.notificationService = notificationService;
        this.identityService = identityService;
        this.orderQueryService = orderQueryService;
    }

    @Override
    public Order reserveOrder(Order order) {
        log.info("Start reserving order");

        if(!identityService.isValidUser(order.getUserId())) {
            throw new IllegalArgumentException("Invalid user");
        }

        log.info("User {} is valid", order.getUserId());

        Double subTotal = eventService.reverseTicketsAndCalculateSubtotal(order.getEventId(), order.getEventDateId(),
                                order.getTicketInOrders());
        if(subTotal == null) {
            throw new IllegalArgumentException("Invalid event or no tickets available");
        }

        log.info("Event {} is valid and reserve tickets successfully", order.getEventId());

        order.setSubTotal(subTotal);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setExpiredAt(LocalDateTime.now().plusMinutes(Constants.ORDER_EXPIRATION_MINUTES));

        orderCommandDao.addOrder(order);

        log.info("Reserve order successfully");

        return order;
    }

    @Override
    public Order payOrder(Order existentOrderInfo) {

        if(!orderQueryService.isValidOrder(existentOrderInfo.getOrderId())) {
            throw new IllegalArgumentException("Invalid order");
        }

        Order order = orderQueryService.getOrderById(existentOrderInfo.getOrderId());

        if(order.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Order is expired");
        }

        order.setTotal(order.getSubTotal() - promotionService.applyPromotion());

        paymentService.processPayment(order);

        order.setOrderStatus(OrderStatus.PAID);
        orderCommandDao.updateOrder(order);

        notificationService.sendOrderConfirmation(order);

        return order;
    }

}
