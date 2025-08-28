package org.pqkkkkk.ticsys.order_service.domain.usecase;

import org.pqkkkkk.ticsys.order_service.domain.entity.Order;

public interface OrderQueryService {
    public Order getOrderById(Long orderId);
    public boolean isValidOrder(Long orderId);
}
