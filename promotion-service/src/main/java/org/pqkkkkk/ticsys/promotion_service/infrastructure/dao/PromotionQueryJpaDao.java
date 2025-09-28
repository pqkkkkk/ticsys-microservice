package org.pqkkkkk.ticsys.promotion_service.infrastructure.dao;

import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionType;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionVisibility;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherType;
import org.pqkkkkk.ticsys.promotion_service.domain.dao.PromotionQueryDao;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;
import org.pqkkkkk.ticsys.promotion_service.infrastructure.dao.repository.PromotionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Override
    public Page<VoucherPromotion> getPromotions(Pageable pageable,
            String nameKeyword, VoucherType voucherType, VoucherPromotionType voucherPromotionType,
            VoucherPromotionVisibility visibility, Long eventId, Boolean isActive) {

        return promotionRepository.getPromotions(pageable, nameKeyword, voucherType,
                                voucherPromotionType, visibility, eventId, isActive);
    }

}
