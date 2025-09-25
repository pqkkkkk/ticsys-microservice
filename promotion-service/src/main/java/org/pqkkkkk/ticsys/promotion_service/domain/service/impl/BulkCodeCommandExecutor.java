package org.pqkkkkk.ticsys.promotion_service.domain.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.pqkkkkk.ticsys.promotion_service.domain.Constants;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionType;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherType;
import org.pqkkkkk.ticsys.promotion_service.domain.dao.PromotionCommandDao;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.BulkCodePromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;
import org.pqkkkkk.ticsys.promotion_service.domain.service.PromotionCommandExecutor;
import org.pqkkkkk.ticsys.promotion_service.domain.service.PromotionQueryService;
import org.pqkkkkk.ticsys.promotion_service.domain.service.external.EventService;
import org.pqkkkkk.ticsys.promotion_service.domain.service.external.IdentityService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service("bulkCodeCommandExecutor")
public class BulkCodeCommandExecutor extends PromotionCommandExecutor {
        private final PromotionCommandDao promotionCommandDao;
    private final EventService eventService;
    private final IdentityService identityService;
    private final PromotionQueryService promotionQueryService;

    public BulkCodeCommandExecutor(PromotionCommandDao promotionCommandDao,
                                    EventService eventService, IdentityService identityService,
                                    PromotionQueryService promotionQueryService) {
        super(promotionCommandDao);
        this.promotionCommandDao = promotionCommandDao;
        this.eventService = eventService;
        this.identityService = identityService;
        this.promotionQueryService = promotionQueryService;
    }
    private boolean isValidPromotionInfo(VoucherPromotion voucherPromotion) {
        if(voucherPromotion.getBulkCodePromotionInfos() == null || voucherPromotion.getBulkCodePromotionInfos().isEmpty()) {
            return false;
        }
        if(voucherPromotion.getVoucherPromotionType() == null || voucherPromotion.getVoucherPromotionType() != VoucherPromotionType.BULK_CODE) {
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
    private List<BulkCodePromotionInfo> generateRandomCodes(int numberOfCodes, String codePrefix) {
        Random random = new Random();

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        
        int subFixLength = Constants.SUBFIX_OF_BULK_CODE_LENGTH;

        List<BulkCodePromotionInfo> bulkCodes = new ArrayList<>();
        
        for (int i = 0; i < numberOfCodes; i++) {
            StringBuilder codeBuilder = new StringBuilder(codePrefix);
            for (int j = 0; j < subFixLength; j++) {
                codeBuilder.append(characters.charAt(random.nextInt(characters.length())));
            }
            bulkCodes.add(BulkCodePromotionInfo.builder()
                            .codePrefix(codePrefix)
                            .codeValue(codeBuilder.toString())
                            .isUsed(false)
                            .build());
        }
        return bulkCodes;
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

        // Generate random codes for each BulkCodePromotionInfo
        List<BulkCodePromotionInfo> bulkCodes = generateRandomCodes(voucherPromotion.getNumberOfCodes(),
                                                voucherPromotion.getBulkCodePromotionInfos().get(0).getCodePrefix());
        voucherPromotion.setBulkCodePromotionInfos(bulkCodes);

        for (BulkCodePromotionInfo info : voucherPromotion.getBulkCodePromotionInfos()) {
            info.setVoucherPromotion(voucherPromotion); // Set the relationship
        }

        voucherPromotion.setTotalAppliedTicketCount(voucherPromotion.getMaxAppliedTicketCountPerCode() * voucherPromotion.getBulkCodePromotionInfos().size());
        
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

        // Update bulk code information
        if (newPromotionInfo.getBulkCodePromotionInfos() != null && !newPromotionInfo.getBulkCodePromotionInfos().isEmpty()) {
            // Handle updating or adding new bulk codes
            // Be careful with this logic to avoid deleting used codes
            existingPromotion.setBulkCodePromotionInfos(newPromotionInfo.getBulkCodePromotionInfos());

            // Update total applied ticket count
            existingPromotion.setTotalAppliedTicketCount(
                newPromotionInfo.getMaxAppliedTicketCountPerCode() * newPromotionInfo.getBulkCodePromotionInfos().size()
            );
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

        if(!eventService.isValidEventDate(newVoucherPromotionInfo.getEventId(), newVoucherPromotionInfo.getEventDateId())) {
            throw new IllegalArgumentException("Invalid event date");
        }
        if(!identityService.isValidUser(newVoucherPromotionInfo.getCreatedBy())) {
            throw new IllegalArgumentException("Invalid user");
        }
        
        return promotionCommandDao.updateVoucherPromotion(newVoucherPromotionInfo);
    }
    @Override
    public VoucherPromotionType getExecutorType() {
        return VoucherPromotionType.BULK_CODE;
    }

}
