package org.pqkkkkk.ticsys.order_service.domain.usecase;

import org.pqkkkkk.ticsys.order_service.domain.entity.Order;

public interface NotificationService {
    public boolean sendOrderConfirmation(Order order);
}
