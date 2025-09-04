package org.pqkkkkk.ticsys.order_service.domain.usecase.impl;

import org.pqkkkkk.ticsys.order_service.domain.Constants.OrderStatus;
import org.pqkkkkk.ticsys.order_service.domain.dao.OrderQueryDao;
import org.pqkkkkk.ticsys.order_service.domain.entity.Order;
import org.pqkkkkk.ticsys.order_service.domain.usecase.OrderQueryService;
import org.springframework.stereotype.Service;

@Service
public class OrderQueryServiceImpl implements OrderQueryService {
    private OrderQueryDao orderQueryDao;

    public OrderQueryServiceImpl(OrderQueryDao orderQueryDao) {
        this.orderQueryDao = orderQueryDao;
    }

    @Override
    public Order getOrderById(Long orderId) {
        Order order = orderQueryDao.getOrderById(orderId);

        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }

        return order;
    }

    @Override
    public boolean isValidOrder(Long orderId) {
        Order order = orderQueryDao.getOrderById(orderId);
        
        if(order == null)
            return false;
        if(order.getOrderStatus() != OrderStatus.PENDING)
            return false;

        return true;
    }

}
