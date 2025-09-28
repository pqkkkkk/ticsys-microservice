package org.pqkkkkk.ticsys.promotion_service.infrastructure.dao;

import org.pqkkkkk.ticsys.promotion_service.domain.dao.PromotionCommandDao;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;
import org.pqkkkkk.ticsys.promotion_service.infrastructure.dao.repository.PromotionRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link PromotionCommandDao} interface that provides
 * operations to manipulate {@link VoucherPromotion} entities.
 */
@Repository
@Slf4j
public class PromotionCommandJpaDao implements PromotionCommandDao {

    private final PromotionRepository voucherPromotionRepository;

    public PromotionCommandJpaDao(PromotionRepository voucherPromotionRepository) {
        this.voucherPromotionRepository = voucherPromotionRepository;
    }

    @Override
    @Transactional
    public boolean deleteVoucherPromotion(Long voucherPromotionId) {
        if (voucherPromotionId == null) {
            throw new IllegalArgumentException("Voucher promotion ID cannot be null");
        }

        if( !voucherPromotionRepository.existsById(voucherPromotionId)) {
            throw new IllegalArgumentException("Voucher promotion with ID " + voucherPromotionId + " does not exist");
        }

        voucherPromotionRepository.deleteById(voucherPromotionId);

        return true;
    }

    @Override
    @Transactional
    public VoucherPromotion createVoucherPromotion(VoucherPromotion voucherPromotion) {
        if (voucherPromotion == null) {
            throw new IllegalArgumentException("Voucher promotion cannot be null");
        }
        if(voucherPromotion.getId() != null && voucherPromotionRepository.existsById(voucherPromotion.getId())) {
            throw new IllegalArgumentException("Voucher promotion with ID " + voucherPromotion.getId() + " already exists");
        }

        VoucherPromotion createdPromotion = voucherPromotionRepository.save(voucherPromotion);

        return createdPromotion;
    }

    @Override
    @Transactional
    public VoucherPromotion updateVoucherPromotion(VoucherPromotion voucherPromotion) {
        if (voucherPromotion == null) {
            throw new IllegalArgumentException("Voucher promotion cannot be null");
        }

        if (voucherPromotion.getId() == null) {
            throw new IllegalArgumentException("Voucher promotion ID cannot be null");
        }

        if (!voucherPromotionRepository.existsById(voucherPromotion.getId())) {
            throw new IllegalArgumentException("Voucher promotion with ID " + voucherPromotion.getId() + " does not exist");
        }

        VoucherPromotion updatedPromotion = voucherPromotionRepository.save(voucherPromotion);
        return updatedPromotion;
    }
}