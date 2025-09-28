package org.pqkkkkk.ticsys.promotion_service.domain.dao;

import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionType;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionVisibility;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherType;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PromotionQueryDao {
    public VoucherPromotion getPromotionById(Long voucherPromotionId);
    public boolean isExistPromotion(Long voucherPromotionId);
    public Page<VoucherPromotion> getPromotions(
        Pageable pageable,
        String nameKeyword,
        VoucherType voucherType,
        VoucherPromotionType voucherPromotionType,
        VoucherPromotionVisibility visibility,
        Long eventId,
        Boolean isActive
    );
}
