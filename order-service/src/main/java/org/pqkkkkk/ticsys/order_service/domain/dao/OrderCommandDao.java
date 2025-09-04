package org.pqkkkkk.ticsys.order_service.domain.dao;

import org.pqkkkkk.ticsys.order_service.domain.entity.Order;

public interface OrderCommandDao {
    public Order addOrder(Order order);
    public Order updateOrder(Order order);
}
