package org.pqkkkkk.ticsys.order_service.infrastructure.client;

import org.pqkkkkk.ticsys.order_service.domain.entity.Order;
import org.pqkkkkk.ticsys.order_service.domain.usecase.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentMockClient implements PaymentService {

    @Override
    public boolean processPayment(Order order) {
        return true;
    }

}
