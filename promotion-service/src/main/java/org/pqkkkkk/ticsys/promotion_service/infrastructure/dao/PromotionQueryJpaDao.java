package org.pqkkkkk.ticsys.promotion_service.infrastructure.dao;

import org.pqkkkkk.ticsys.promotion_service.domain.dao.PromotionQueryDao;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;
import org.pqkkkkk.ticsys.promotion_service.infrastructure.dao.repository.PromotionRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PromotionQueryJpaDao implements PromotionQueryDao {
    private final PromotionRepository promotionRepository;

    public PromotionQueryJpaDao(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }
    @Override
    public VoucherPromotion getPromotionById(Long voucherPromotionId) {
        return promotionRepository.findById(voucherPromotionId).orElse(null);
    }

    @Override
    public boolean isExistPromotion(Long voucherPromotionId) {
        return promotionRepository.existsById(voucherPromotionId);
    }

}
