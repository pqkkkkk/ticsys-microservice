package org.pqkkkkk.ticsys.promotion_service.infrastructure.dao.repository;

import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionType;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionVisibility;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherType;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for VoucherPromotion entities.
 */
@Repository
public interface PromotionRepository extends JpaRepository<VoucherPromotion, Long> {

    @Query(value = """
        SELECT vp FROM VoucherPromotion vp
        WHERE (:nameKeyword IS NULL OR vp.promotionName LIKE %:nameKeyword%)
            AND (:voucherType IS NULL OR vp.voucherType = :voucherType)
            AND (:voucherPromotionType IS NULL OR vp.voucherPromotionType = :voucherPromotionType)
            AND (:visibility IS NULL OR vp.promotionVisibility = :visibility)
            AND (:eventId IS NULL OR vp.eventId = :eventId)
            """)
    public Page<VoucherPromotion> getPromotions(
        Pageable pageable,
        String nameKeyword, VoucherType voucherType, VoucherPromotionType voucherPromotionType,
        VoucherPromotionVisibility visibility, Long eventId, Boolean isActive
    );
}