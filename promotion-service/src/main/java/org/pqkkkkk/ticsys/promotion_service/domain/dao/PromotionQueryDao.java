package org.pqkkkkk.ticsys.promotion_service.domain.dao;

import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;

public interface PromotionQueryDao {
    public VoucherPromotion getPromotionById(Long voucherPromotionId);
    public boolean isExistPromotion(Long voucherPromotionId);
}
