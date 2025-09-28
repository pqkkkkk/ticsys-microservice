package org.pqkkkkk.ticsys.order_service.domain.usecase;

import org.pqkkkkk.ticsys.order_service.domain.entity.Order;
import org.pqkkkkk.ticsys.order_service.domain.entity.PayOrderCommand;

public interface OrderService {
    public Order reserveOrder(Order order);
    public Order payOrder(PayOrderCommand command);
}
