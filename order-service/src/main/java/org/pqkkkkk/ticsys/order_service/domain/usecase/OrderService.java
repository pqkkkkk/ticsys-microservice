package org.pqkkkkk.ticsys.order_service.domain.usecase;

import org.pqkkkkk.ticsys.order_service.domain.entity.Order;

public interface OrderService {
    public Order reverseOrder(Order order);
    public Order payOrder(Order order);
}
