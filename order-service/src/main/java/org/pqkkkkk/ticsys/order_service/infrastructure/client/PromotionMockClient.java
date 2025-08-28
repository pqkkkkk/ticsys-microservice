package org.pqkkkkk.ticsys.order_service.infrastructure.client;

import org.pqkkkkk.ticsys.order_service.domain.usecase.PromotionService;
import org.springframework.stereotype.Service;

@Service
public class PromotionMockClient implements PromotionService {

    @Override
    public boolean isValidPromotion(Long promotionId) {
        return true;
    }

    @Override
    public Double applyPromotion() {
        return 0.0;
    }

}
