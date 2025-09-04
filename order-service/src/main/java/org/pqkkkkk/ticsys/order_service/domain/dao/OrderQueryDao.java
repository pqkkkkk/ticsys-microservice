package org.pqkkkkk.ticsys.order_service.domain.dao;

import org.pqkkkkk.ticsys.order_service.domain.entity.Order;

public interface OrderQueryDao {
    public Order getOrderById(Long orderId);
}
