package org.pqkkkkk.ticsys.promotion_service.domain.service.impl;

import java.time.LocalDateTime;

import org.pqkkkkk.ticsys.promotion_service.domain.dao.PromotionQueryDao;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;
import org.pqkkkkk.ticsys.promotion_service.domain.service.PromotionQueryService;
import org.springframework.stereotype.Service;

@Service
public class PromotionQueryServiceImpl implements PromotionQueryService {
    private final PromotionQueryDao promotionQueryDao;

    public PromotionQueryServiceImpl(PromotionQueryDao promotionQueryDao) {
        this.promotionQueryDao = promotionQueryDao;
    }

    private boolean isNotInActiveTime(VoucherPromotion vp) {
        LocalDateTime now = LocalDateTime.now();

        if(vp.getEndDate() != null && now.isAfter(vp.getEndDate())) {
            return false;
        }
        return true;
    }
    @Override
    public boolean isValidPromotion(Long voucherPromotionId) {
        VoucherPromotion vp = promotionQueryDao.getPromotionById(voucherPromotionId);
        if(vp == null) {
            return false;
        }

        if(!isNotInActiveTime(vp)) {
            return false;
        }

        return true;
    }

    @Override
    public VoucherPromotion getPromotionById(Long voucherPromotionId) {
        VoucherPromotion vp = promotionQueryDao.getPromotionById(voucherPromotionId);

        if(vp == null){
            throw new IllegalArgumentException("Promotion with ID " + voucherPromotionId + " not found");
        }

        return vp;
    }

    @Override
    public VoucherPromotion getValidPromotion(Long voucherPromotionId) {
        VoucherPromotion vp = promotionQueryDao.getPromotionById(voucherPromotionId);
        if(vp == null) {
            throw new IllegalArgumentException("Promotion with ID " + voucherPromotionId + " not found");
        }

        if(!isNotInActiveTime(vp)) {
            throw new IllegalArgumentException("Promotion with ID " + voucherPromotionId + " is not valid");
        }

        return vp;
    }

}
