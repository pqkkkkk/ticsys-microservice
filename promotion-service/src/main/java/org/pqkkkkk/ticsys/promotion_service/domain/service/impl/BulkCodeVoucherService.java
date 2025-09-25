package org.pqkkkkk.ticsys.promotion_service.domain.service.impl;

import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionType;
import org.pqkkkkk.ticsys.promotion_service.domain.dao.BulkCodePromotionInfoDao;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.ApplyPromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.BulkCodePromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;
import org.pqkkkkk.ticsys.promotion_service.domain.service.VoucherService;
import org.springframework.stereotype.Service;

@Service
public class BulkCodeVoucherService implements VoucherService {
    private final BulkCodePromotionInfoDao bulkCodeVoucherDao;

    public BulkCodeVoucherService(BulkCodePromotionInfoDao bulkCodeVoucherDao) {
        this.bulkCodeVoucherDao = bulkCodeVoucherDao;
    }
    @Override
    public boolean isApplicable(VoucherPromotion voucherPromotion, ApplyPromotionInfo applyPromotionInfo) {
        BulkCodePromotionInfo info = voucherPromotion.getBulkCodePromotionInfos().stream()
                                                        .filter(i -> (i.getCodePrefix() + i.getCodeValue()).equals(applyPromotionInfo.getVoucherCode()))
                                                        .findFirst()
                                                        .orElse(null);

        return info != null && info.getIsUsed() == false;
    }

    @Override
    public Double calculateVoucherDiscount(VoucherPromotion voucherPromotion, ApplyPromotionInfo applyPromotionInfo) {
        switch (voucherPromotion.getVoucherType()) {
            case BY_AMOUNT:
                Integer appliedTicketQuantity = Math.min(applyPromotionInfo.getTicketQuantity(),
                    voucherPromotion.getMaxAppliedTicketCountPerCode());
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
        return VoucherPromotionType.BULK_CODE;
    }

    @Override
    public boolean markVoucherAsUsed(VoucherPromotion voucherPromotion, ApplyPromotionInfo applyPromotionInfo) {
        BulkCodePromotionInfo info = voucherPromotion.getBulkCodePromotionInfos().stream()
                            .filter(i -> (i.getCodePrefix() + i.getCodeValue()).equals(applyPromotionInfo.getVoucherCode()))
                            .findFirst()
                            .orElse(null);
                            
        info.setIsUsed(true);

        bulkCodeVoucherDao.updateBulkCodePromotionInfo(info);

        return true;
    }

}
