package org.pqkkkkk.ticsys.promotion_service.domain.service;

import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionType;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.ApplyPromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;

public interface VoucherService {
    public boolean isApplicable(VoucherPromotion voucherPromotion, ApplyPromotionInfo applyPromotionInfo);
    public Double calculateVoucherDiscount(VoucherPromotion voucherPromotion, ApplyPromotionInfo applyPromotionInfo);
    public VoucherPromotionType getVoucherType();
    public boolean markVoucherAsUsed(VoucherPromotion voucherPromotion, ApplyPromotionInfo applyPromotionInfo);
}
