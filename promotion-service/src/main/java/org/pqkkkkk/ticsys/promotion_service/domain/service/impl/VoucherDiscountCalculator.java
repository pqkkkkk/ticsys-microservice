package org.pqkkkkk.ticsys.promotion_service.domain.service.impl;

import java.util.Map;

import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionType;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.ApplyPromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;
import org.pqkkkkk.ticsys.promotion_service.domain.service.PromotionCalculatorService;
import org.pqkkkkk.ticsys.promotion_service.domain.service.PromotionQueryService;
import org.pqkkkkk.ticsys.promotion_service.domain.service.VoucherService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class VoucherDiscountCalculator implements PromotionCalculatorService {
    private final PromotionQueryService promotionQueryService;
    private final Map<VoucherPromotionType, VoucherService> voucherServices;

    public VoucherDiscountCalculator(PromotionQueryService promotionQueryService,
                                    Map<VoucherPromotionType, VoucherService> voucherQueryServices) {
        this.promotionQueryService = promotionQueryService;
        this.voucherServices = voucherQueryServices;
    }
    private VoucherService getVoucherService(VoucherPromotionType voucherPromotionType){
        VoucherService service = voucherServices.get(voucherPromotionType);

        if(service == null){
            throw new IllegalArgumentException("Invalid voucher promotion type");
        }
        return service;
    }
    @Override
    @Transactional
    public Double calculateDiscount(ApplyPromotionInfo applyPromotionInfo) {
        VoucherPromotion promotion = promotionQueryService.getValidPromotion(applyPromotionInfo.getPromotionId());

        if(!getVoucherService(promotion.getVoucherPromotionType()).isApplicable(promotion, applyPromotionInfo)){
            throw new IllegalStateException("Voucher is not valid for the given conditions");
        }

        Double discount = getVoucherService(promotion.getVoucherPromotionType())
                .calculateVoucherDiscount(promotion, applyPromotionInfo);
                
        boolean updateResult = getVoucherService(promotion.getVoucherPromotionType())
                .markVoucherAsUsed(promotion, applyPromotionInfo);

        if(!updateResult){
            throw new RuntimeException("Failed to update voucher status after applying discount");
        }
        
        return discount;
    }

}