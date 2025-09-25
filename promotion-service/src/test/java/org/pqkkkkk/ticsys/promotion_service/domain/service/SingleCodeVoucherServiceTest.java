package org.pqkkkkk.ticsys.promotion_service.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherType;
import org.pqkkkkk.ticsys.promotion_service.domain.dao.PromotionCommandDao;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.ApplyPromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.SingleCodePromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;
import org.pqkkkkk.ticsys.promotion_service.domain.service.impl.SingleCodeVoucherService;

@ExtendWith(MockitoExtension.class)
public class SingleCodeVoucherServiceTest {
    @Mock
    private PromotionCommandDao promotionCommandDao;

    @InjectMocks
    private SingleCodeVoucherService singleCodeVoucherService;

    private VoucherPromotion voucherPromotion;
    private ApplyPromotionInfo applyPromotionInfo;

    // Test cases for isValidVoucher
    @Test
    public void isApplicable_NullVoucherCode_ReturnsFalse() {
        voucherPromotion = new VoucherPromotion();
        voucherPromotion.setCurrentAppliedTicketCount(0);
        voucherPromotion.setMaxAppliedTicketCountPerCode(5);
        voucherPromotion.setSingleCodePromotionInfo(
            SingleCodePromotionInfo.builder().minTicketQuantityPerOrderToApply(1).build()
        );

        applyPromotionInfo = new ApplyPromotionInfo();
        applyPromotionInfo.setVoucherCode(null);

        boolean result = singleCodeVoucherService.isApplicable(voucherPromotion, applyPromotionInfo);
        assert(!result);
    }
    @Test
    public void isApplicable_InvalidVoucherCode_ReturnsFalse() {
        voucherPromotion = new VoucherPromotion();
        voucherPromotion.setCurrentAppliedTicketCount(0);
        voucherPromotion.setMaxAppliedTicketCountPerCode(5);
        voucherPromotion.setSingleCodePromotionInfo(
            SingleCodePromotionInfo.builder().codeValue("VALIDCODE").minTicketQuantityPerOrderToApply(1).build()
        );

        applyPromotionInfo = new ApplyPromotionInfo();
        applyPromotionInfo.setVoucherCode("INVALIDCODE");

        boolean result = singleCodeVoucherService.isApplicable(voucherPromotion, applyPromotionInfo);
        assert(!result);
    }
    @Test
    public void isApplicable_WithinLimitButTicketQuantityLessThanMin_ReturnsTrue() {
        voucherPromotion = new VoucherPromotion();
        voucherPromotion.setCurrentAppliedTicketCount(2);
        voucherPromotion.setMaxAppliedTicketCountPerCode(5);
        voucherPromotion.setSingleCodePromotionInfo(
            SingleCodePromotionInfo.builder().minTicketQuantityPerOrderToApply(2).build()
        );

        applyPromotionInfo = new ApplyPromotionInfo();
        applyPromotionInfo.setTicketQuantity(1);
        boolean result = singleCodeVoucherService.isApplicable(voucherPromotion, applyPromotionInfo);
        assert(!result);
    }
    @Test
    public void isApplicable_ExceedsLimit_ReturnsFalse() {
        voucherPromotion = new VoucherPromotion();
        voucherPromotion.setCurrentAppliedTicketCount(5);
        voucherPromotion.setMaxAppliedTicketCountPerCode(5);

        applyPromotionInfo = new ApplyPromotionInfo();

        boolean result = singleCodeVoucherService.isApplicable(voucherPromotion, applyPromotionInfo);
        assert(!result);
    }
    @Test
    public void isApplicable_WithinLimitAndTicketQuantityMeetsMin_ReturnsTrue() {
        voucherPromotion = new VoucherPromotion();
        voucherPromotion.setCurrentAppliedTicketCount(3);
        voucherPromotion.setMaxAppliedTicketCountPerCode(5);
        voucherPromotion.setSingleCodePromotionInfo(
            SingleCodePromotionInfo.builder()
            .codeValue("VALIDCODE")
            .minTicketQuantityPerOrderToApply(2).build()
        );

        applyPromotionInfo = new ApplyPromotionInfo();
        applyPromotionInfo.setTicketQuantity(2);
        applyPromotionInfo.setVoucherCode("VALIDCODE");

        boolean result = singleCodeVoucherService.isApplicable(voucherPromotion, applyPromotionInfo);
        assertTrue(result);
    }
    // Test cases for calculateVoucherDiscount
    @Test
    public void calculateVoucherDiscount_ByAmountWithTicketQuantityLessThanMaxAppliedQuantityPerCode_ReturnsCorrectDiscount() {
        voucherPromotion = new VoucherPromotion();
        voucherPromotion.setVoucherType(VoucherType.BY_AMOUNT);
        voucherPromotion.setVoucherValueByAmount(10.0);
        voucherPromotion.setSingleCodePromotionInfo(
            SingleCodePromotionInfo.builder().minTicketQuantityPerOrderToApply(1)
            .maxTicketQuantityPerOrderToApply(5)
            .build()
        );

        applyPromotionInfo = new ApplyPromotionInfo();
        applyPromotionInfo.setTicketQuantity(3);
        applyPromotionInfo.setOriginalPrice(100.0);

        Double discount = singleCodeVoucherService.calculateVoucherDiscount(voucherPromotion, applyPromotionInfo);
        assert(discount.equals(30.0));
    }
    @Test
    public void calculateVoucherDiscount_ByAmountWithTicketQuantityExceedsMaxAppliedQuantityPerCode_ReturnsCorrectDiscount() {
        voucherPromotion = new VoucherPromotion();
        voucherPromotion.setVoucherType(VoucherType.BY_AMOUNT);
        voucherPromotion.setVoucherValueByAmount(15.0);
        voucherPromotion.setSingleCodePromotionInfo(
            SingleCodePromotionInfo.builder().minTicketQuantityPerOrderToApply(1)
            .maxTicketQuantityPerOrderToApply(4)
            .build()
        );

        applyPromotionInfo = new ApplyPromotionInfo();
        applyPromotionInfo.setTicketQuantity(6);
        applyPromotionInfo.setOriginalPrice(100.0);

        Double discount = singleCodeVoucherService.calculateVoucherDiscount(voucherPromotion, applyPromotionInfo);
        assert(discount.equals(60.0));
    }
    @Test
    public void calculateVoucherDiscount_ByPercentage_ReturnsCorrectDiscount() {
        voucherPromotion = new VoucherPromotion();
        voucherPromotion.setVoucherType(VoucherType.BY_PERCENTAGE);
        voucherPromotion.setVoucherValueByPercentage(20.0);

        applyPromotionInfo = new ApplyPromotionInfo();
        applyPromotionInfo.setOriginalPrice(50.0);
        applyPromotionInfo.setTicketQuantity(2);

        /* TODO: calucate based on original price currently.
         * Need to get the price of each ticket from another service
         * to calculate the discount for each ticket instead of using original price of order
         */
        Double discount = singleCodeVoucherService.calculateVoucherDiscount(voucherPromotion, applyPromotionInfo);
        assertEquals(10.0, discount);
    }
}
