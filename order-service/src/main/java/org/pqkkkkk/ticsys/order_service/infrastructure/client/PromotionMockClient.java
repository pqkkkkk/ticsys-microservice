package org.pqkkkkk.ticsys.order_service.infrastructure.client;

import org.pqkkkkk.ticsys.order_service.domain.entity.ApplyPromotionInfo;
import org.pqkkkkk.ticsys.order_service.domain.usecase.PromotionService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class PromotionMockClient implements PromotionService {

    @Override
    public Double applyPromotion(ApplyPromotionInfo applyPromotionInfo) {
        return 0.0;
    }

}
