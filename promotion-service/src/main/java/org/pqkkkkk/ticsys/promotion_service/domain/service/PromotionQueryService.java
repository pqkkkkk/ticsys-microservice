package org.pqkkkkk.ticsys.promotion_service.domain.service;

import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;

public interface PromotionQueryService {
    /**
     * Check if a promotion is valid
     * @param voucherPromotionId
     * @return {@link VoucherPromotion} if valid, null otherwise
     */
    public VoucherPromotion getValidPromotion(Long voucherPromotionId);
    public boolean isValidPromotion(Long voucherPromotionId);
    public VoucherPromotion getPromotionById(Long voucherPromotionId);
}
