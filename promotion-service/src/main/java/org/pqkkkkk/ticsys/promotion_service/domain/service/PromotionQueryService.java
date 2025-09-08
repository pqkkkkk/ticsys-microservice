package org.pqkkkkk.ticsys.promotion_service.domain.service;

import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;

public interface PromotionQueryService {
    public boolean isValidPromotion(Long voucherPromotionId);
    public VoucherPromotion getPromotionById(Long voucherPromotionId);
}
