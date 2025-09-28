package org.pqkkkkk.ticsys.promotion_service.infrastructure.dao.repository;

import org.pqkkkkk.ticsys.promotion_service.domain.entity.BulkCodePromotionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BulkCodePromotionInfoRepository extends JpaRepository<BulkCodePromotionInfo, Long> {

}
