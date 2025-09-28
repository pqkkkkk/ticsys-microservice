package org.pqkkkkk.ticsys.promotion_service.domain.dao;

import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;

public interface PromotionCommandDao {
    public boolean deleteVoucherPromotion(Long voucherPromotionId);
    public VoucherPromotion createVoucherPromotion(VoucherPromotion voucherPromotion);
    public VoucherPromotion updateVoucherPromotion(VoucherPromotion voucherPromotion);
}
