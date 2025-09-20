package org.pqkkkkk.ticsys.promotion_service.infrastructure.dao.repository;

import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for VoucherPromotion entities.
 */
@Repository
public interface PromotionRepository extends JpaRepository<VoucherPromotion, Long> {
    // Spring Data JPA will automatically provide basic CRUD operations
    // Additional custom queries can be added here if needed
}