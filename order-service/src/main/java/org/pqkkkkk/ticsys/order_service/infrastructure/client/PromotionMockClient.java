package org.pqkkkkk.ticsys.order_service.infrastructure.client;

import org.pqkkkkk.ticsys.order_service.domain.entity.ApplyPromotionInfo;
import org.pqkkkkk.ticsys.order_service.domain.usecase.PromotionService;
import org.springframework.stereotype.Service;

@Service
public class PromotionMockClient implements PromotionService {

    @Override
    public Double applyPromotion(ApplyPromotionInfo applyPromotionInfo) {
        return 0.0;
    }

}
