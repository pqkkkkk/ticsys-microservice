package org.pqkkkkk.ticsys.promotion_service.domain.service.impl;

import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionType;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherType;
import org.pqkkkkk.ticsys.promotion_service.domain.dao.PromotionCommandDao;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;
import org.pqkkkkk.ticsys.promotion_service.domain.service.PromotionCommandExecutor;
import org.pqkkkkk.ticsys.promotion_service.domain.service.PromotionQueryService;
import org.pqkkkkk.ticsys.promotion_service.domain.service.external.EventService;
import org.pqkkkkk.ticsys.promotion_service.domain.service.external.IdentityService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service("singleCodeCommandExecutor")
public class SingleCodeCommandExecutor extends PromotionCommandExecutor {
    private final PromotionCommandDao promotionCommandDao;
    private final EventService eventService;
    private final IdentityService identityService;
    private final PromotionQueryService promotionQueryService;

    public SingleCodeCommandExecutor(PromotionCommandDao promotionCommandDao,
                                    EventService eventService, IdentityService identityService,
                                    PromotionQueryService promotionQueryService) {
        super(promotionCommandDao);
        this.promotionCommandDao = promotionCommandDao;
        this.eventService = eventService;
        this.identityService = identityService;
        this.promotionQueryService = promotionQueryService;
    }
    private boolean isValidPromotionInfo(VoucherPromotion voucherPromotion) {
        if(voucherPromotion.getSingleCodePromotionInfo() == null) {
            return false;
        }
        if(voucherPromotion.getVoucherPromotionType() == null || voucherPromotion.getVoucherPromotionType() != VoucherPromotionType.SINGLE_CODE) {
            return false;
        }
        if(voucherPromotion.getVoucherType() == VoucherType.BY_PERCENTAGE && voucherPromotion.getVoucherValueByPercentage() == null) {
            return false;
        }
        if(voucherPromotion.getVoucherType() == VoucherType.BY_AMOUNT && voucherPromotion.getVoucherValueByAmount() == null) {
            return false;
        }
        return true;
    }
    @Override
    @Transactional
    public VoucherPromotion createVoucherPromotion(VoucherPromotion voucherPromotion) {
        if(!isValidPromotionInfo(voucherPromotion)) {
            throw new IllegalArgumentException("Invalid promotion information");
        }
        if(!eventService.isValidEventDate(voucherPromotion.getEventId(), voucherPromotion.getEventDateId())) {
            throw new IllegalArgumentException("Invalid event date");
        }
        if(!identityService.isValidUser(voucherPromotion.getCreatedBy())) {
            throw new IllegalArgumentException("Invalid user");
        }

        // Initialize current applied ticket count to 0
        voucherPromotion.setCurrentAppliedTicketCount(0);
        
        return promotionCommandDao.createVoucherPromotion(voucherPromotion);
    }

    private void updateVoucherPromotionFields(VoucherPromotion existingPromotion, VoucherPromotion newPromotionInfo) {
        // Update basic fields
        if (newPromotionInfo.getPromotionName() != null) {
            existingPromotion.setPromotionName(newPromotionInfo.getPromotionName());
        }
        if (newPromotionInfo.getPromotionDescription() != null) {
            existingPromotion.setPromotionDescription(newPromotionInfo.getPromotionDescription());
        }
        if (newPromotionInfo.getStartDate() != null) {
            existingPromotion.setStartDate(newPromotionInfo.getStartDate());
        }
        if (newPromotionInfo.getEndDate() != null) {
            existingPromotion.setEndDate(newPromotionInfo.getEndDate());
        }
        if(newPromotionInfo.getPromotionVisibility() != null) {
            existingPromotion.setPromotionVisibility(newPromotionInfo.getPromotionVisibility());
        }

        // Update voucher information
        if (newPromotionInfo.getVoucherType() != null) {
            existingPromotion.setVoucherType(newPromotionInfo.getVoucherType());
        }
        if (newPromotionInfo.getVoucherValueByPercentage() != null) {
            existingPromotion.setVoucherValueByPercentage(newPromotionInfo.getVoucherValueByPercentage());
        }
        if (newPromotionInfo.getVoucherValueByAmount() != null) {
            existingPromotion.setVoucherValueByAmount(newPromotionInfo.getVoucherValueByAmount());
        }

        // Update single code information
        if (newPromotionInfo.getSingleCodePromotionInfo() != null) {
            existingPromotion.setSingleCodePromotionInfo(newPromotionInfo.getSingleCodePromotionInfo());
        }

        // Update other information
        if (newPromotionInfo.getEventId() != null) {
            existingPromotion.setEventId(newPromotionInfo.getEventId());
        }
        if (newPromotionInfo.getEventDateId() != null) {
            existingPromotion.setEventDateId(newPromotionInfo.getEventDateId());
        }

        // Update information of the editor
        existingPromotion.setUpdatedBy(newPromotionInfo.getUpdatedBy());
    }
    @Override
    @Transactional
    public VoucherPromotion updateVoucherPromotion(VoucherPromotion newVoucherPromotionInfo) {
        if(!promotionQueryService.isValidPromotion(newVoucherPromotionInfo.getId())) {
            throw new IllegalArgumentException("Promotion is not valid");
        }

        VoucherPromotion existingPromotion = promotionQueryService.getPromotionById(newVoucherPromotionInfo.getId());

        updateVoucherPromotionFields(existingPromotion, newVoucherPromotionInfo);

        if(!isValidPromotionInfo(existingPromotion)) {
            throw new IllegalArgumentException("Invalid promotion information");
        }

        if(!eventService.isValidEventDate(existingPromotion.getEventId(), existingPromotion.getEventDateId())) {
            throw new IllegalArgumentException("Invalid event date");
        }
        if(!identityService.isValidUser(existingPromotion.getUpdatedBy())) {
            throw new IllegalArgumentException("Invalid user");
        }
        
        return promotionCommandDao.updateVoucherPromotion(existingPromotion);
    }
    @Override
    public VoucherPromotionType getExecutorType() {
        return VoucherPromotionType.SINGLE_CODE;
    }

}
