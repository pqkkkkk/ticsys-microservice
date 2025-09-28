package org.pqkkkkk.ticsys.promotion_service.domain.service;

import org.pqkkkkk.ticsys.promotion_service.domain.entity.ApplyPromotionInfo;

public interface PromotionCalculatorService {
    public Double useVoucherPromotion(ApplyPromotionInfo applyPromotionInfo);
    public Double calculateDiscount(ApplyPromotionInfo applyPromotionInfo);
}
