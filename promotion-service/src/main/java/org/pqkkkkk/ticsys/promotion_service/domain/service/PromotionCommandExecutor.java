package org.pqkkkkk.ticsys.promotion_service.domain.service;

import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionType;
import org.pqkkkkk.ticsys.promotion_service.domain.dao.PromotionCommandDao;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;

public abstract class PromotionCommandExecutor {
    private final PromotionCommandDao promotionCommandDao;

    public PromotionCommandExecutor(PromotionCommandDao promotionCommandDao) {
        this.promotionCommandDao = promotionCommandDao;
    }

    public abstract VoucherPromotion createVoucherPromotion(VoucherPromotion voucherPromotion);
    public abstract VoucherPromotion updateVoucherPromotion(VoucherPromotion voucherPromotion);

    public abstract VoucherPromotionType getExecutorType();

    public boolean deleteVoucherPromotion(Long voucherPromotionId) {
        return promotionCommandDao.deleteVoucherPromotion(voucherPromotionId);
    }
}
