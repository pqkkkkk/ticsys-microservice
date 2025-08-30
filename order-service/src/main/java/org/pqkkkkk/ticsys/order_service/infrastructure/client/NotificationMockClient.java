package org.pqkkkkk.ticsys.order_service.infrastructure.client;

import org.pqkkkkk.ticsys.order_service.domain.entity.Order;
import org.pqkkkkk.ticsys.order_service.domain.usecase.NotificationService;
import org.springframework.stereotype.Service;

@Service("notificationMockClient")
public class NotificationMockClient implements NotificationService {

    @Override
    public boolean sendOrderConfirmation(Order order) {
        return true;
    }

}
