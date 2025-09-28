package org.pqkkkkk.ticsys.promotion_service.domain.service.impl;

import java.time.LocalDateTime;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionType;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionVisibility;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherType;
import org.pqkkkkk.ticsys.promotion_service.domain.dao.PromotionQueryDao;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;
import org.pqkkkkk.ticsys.promotion_service.domain.service.PromotionQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PromotionQueryServiceImpl implements PromotionQueryService {
    private final PromotionQueryDao promotionQueryDao;

    public PromotionQueryServiceImpl(PromotionQueryDao promotionQueryDao) {
        this.promotionQueryDao = promotionQueryDao;
    }

    private boolean isInActiveTime(VoucherPromotion vp) {
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

        if(!isInActiveTime(vp)) {
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

        if(!isInActiveTime(vp)) {
            throw new IllegalArgumentException("Promotion with ID " + voucherPromotionId + " is not active");
        }

        return vp;
    }

    @Override
    public Page<VoucherPromotion> getPromotions(Integer currentPage, Integer pageSize, String sortBy,
            String sortDirection, String nameKeyword, VoucherType voucherType, VoucherPromotionType voucherPromotionType,
            VoucherPromotionVisibility visibility, Long eventId, Boolean isActive) {
        Pageable pageable = createPageable(currentPage, pageSize, sortBy, sortDirection);

        return promotionQueryDao.getPromotions(pageable, nameKeyword, voucherType,
                                voucherPromotionType, visibility, eventId, isActive);
    }
    private Pageable createPageable(Integer currentPage, Integer pageSize, String sortBy,
            String sortDirection) {
                
        sortBy = (sortBy == null || sortBy.isBlank()) ? Constants.DEFAULT_SORT_BY : sortBy;
        sortDirection = (sortDirection == null || sortDirection.isBlank()) ? Constants.DEFAULT_SORT_DIRECTION : sortDirection;
        Sort sort = Sort.by(sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);

        if(currentPage == null || pageSize == null) {
            return PageRequest.of(0, Constants.DEFAULT_PAGE_SIZE, sort);
        }
        return PageRequest.of(currentPage - 1, pageSize, sort); // -1 because pages are 0-indexed
    }

}
