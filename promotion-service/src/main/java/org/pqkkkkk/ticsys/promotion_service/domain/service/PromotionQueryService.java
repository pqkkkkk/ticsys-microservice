package org.pqkkkkk.ticsys.promotion_service.domain.service;

import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionType;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionVisibility;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherType;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;
import org.springframework.data.domain.Page;

public interface PromotionQueryService {
    public VoucherPromotion getValidPromotion(Long voucherPromotionId);
    public boolean isValidPromotion(Long voucherPromotionId);
    public VoucherPromotion getPromotionById(Long voucherPromotionId);
    public Page<VoucherPromotion> getPromotions(
        Integer currentPage,
        Integer pageSize,
        String sortBy,
        String sortDirection,
        String nameKeyword,
        VoucherType voucherType,
        VoucherPromotionType voucherPromotionType,
        VoucherPromotionVisibility visibility,
        Long eventId,
        Boolean isActive
    );
}
