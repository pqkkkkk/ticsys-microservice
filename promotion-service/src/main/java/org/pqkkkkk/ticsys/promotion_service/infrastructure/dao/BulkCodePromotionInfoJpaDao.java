package org.pqkkkkk.ticsys.promotion_service.infrastructure.dao;

import org.pqkkkkk.ticsys.promotion_service.domain.dao.BulkCodePromotionInfoDao;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.BulkCodePromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.infrastructure.dao.repository.BulkCodePromotionInfoRepository;
import org.springframework.stereotype.Repository;

@Repository
public class BulkCodePromotionInfoJpaDao implements BulkCodePromotionInfoDao {
    private final BulkCodePromotionInfoRepository repository;

    public BulkCodePromotionInfoJpaDao(BulkCodePromotionInfoRepository repository) {
        this.repository = repository;
    }
    @Override
    public BulkCodePromotionInfo updateBulkCodePromotionInfo(BulkCodePromotionInfo info) {
        if(info == null) {
            throw new IllegalArgumentException("BulkCodePromotionInfo cannot be null");
        }
        if(info.getBcpiId() == null) {
            throw new IllegalArgumentException("BulkCodePromotionInfo ID cannot be null for update");
        }

        return repository.save(info);
    }

}
