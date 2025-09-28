package org.pqkkkkk.ticsys.promotion_service.domain.service.impl;

import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionType;
import org.pqkkkkk.ticsys.promotion_service.domain.dao.PromotionCommandDao;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.ApplyPromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;
import org.pqkkkkk.ticsys.promotion_service.domain.service.VoucherService;
import org.springframework.stereotype.Service;

@Service
public class SingleCodeVoucherService implements VoucherService {
    private final PromotionCommandDao promotionCommandDao;

    public SingleCodeVoucherService(PromotionCommandDao promotionCommandDao) {
        this.promotionCommandDao = promotionCommandDao;
    }
    @Override
    public boolean isApplicable(VoucherPromotion voucherPromotion, ApplyPromotionInfo applyPromotionInfo) {
        if(applyPromotionInfo.getVoucherCode() == null || !applyPromotionInfo.getVoucherCode().equals(voucherPromotion.getSingleCodePromotionInfo().getCodeValue())){
            return false;
        }
        if(voucherPromotion.getCurrentAppliedTicketCount() >= voucherPromotion.getMaxAppliedTicketCountPerCode()){
            return false;
        }
        if(applyPromotionInfo.getTicketQuantity() < voucherPromotion.getSingleCodePromotionInfo().getMinTicketQuantityPerOrderToApply()){
            return false;
        }

        return true;
    }

    @Override
    public Double calculateVoucherDiscount(VoucherPromotion voucherPromotion, ApplyPromotionInfo applyPromotionInfo) {
        switch (voucherPromotion.getVoucherType()) {
            case BY_AMOUNT:
                Integer appliedTicketQuantity = Math.min(
                    applyPromotionInfo.getTicketQuantity(),
                    voucherPromotion.getSingleCodePromotionInfo().getMaxTicketQuantityPerOrderToApply()
                );
                return voucherPromotion.getVoucherValueByAmount() * appliedTicketQuantity;
            case BY_PERCENTAGE:
                /* TODO: Need to get the price of each ticket
                from another service to calculate the discount
                for each ticket instead of using original price of order */
                return applyPromotionInfo.getOriginalPrice() * (voucherPromotion.getVoucherValueByPercentage() / 100);
            default:
                throw new IllegalArgumentException("Invalid discount type");
        }
    }

    @Override
    public VoucherPromotionType getVoucherType() {
        return VoucherPromotionType.SINGLE_CODE;
    }

    @Override
    public boolean markVoucherAsUsed(VoucherPromotion voucherPromotion, ApplyPromotionInfo applyPromotionInfo) {
        voucherPromotion.setCurrentAppliedTicketCount(voucherPromotion.getCurrentAppliedTicketCount() + 1);
        promotionCommandDao.updateVoucherPromotion(voucherPromotion);

        return true;
    }

}
