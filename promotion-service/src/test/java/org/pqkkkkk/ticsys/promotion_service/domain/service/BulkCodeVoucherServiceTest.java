package org.pqkkkkk.ticsys.promotion_service.domain.service;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherType;
import org.pqkkkkk.ticsys.promotion_service.domain.dao.BulkCodePromotionInfoDao;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.ApplyPromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.BulkCodePromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;
import org.pqkkkkk.ticsys.promotion_service.domain.service.impl.BulkCodeVoucherService;

@ExtendWith(MockitoExtension.class)
public class BulkCodeVoucherServiceTest {
    @Mock
    private BulkCodePromotionInfoDao bulkCodePromotionInfoDao;
    @InjectMocks
    private BulkCodeVoucherService bulkCodeVoucherService;

    // Test cases for isApplicable
    @Test
    public void isApplicable_IncorrectCode_ReturnsFalse() {
        VoucherPromotion voucherPromotion = VoucherPromotion.builder()
                .bulkCodePromotionInfos(List.of(
                    BulkCodePromotionInfo.builder().codePrefix("PROMO").codeValue("123").isUsed(false).build()
                ))
                .build();

        ApplyPromotionInfo applyPromotionInfo = ApplyPromotionInfo.builder()
                .voucherCode("INVALID_CODE")
                .build();

        boolean result = bulkCodeVoucherService.isApplicable(voucherPromotion, applyPromotionInfo);
        assertFalse(result);
    }
    @Test
    public void isApplicable_AlreadyUsedCode_ReturnsFalse() {
        VoucherPromotion voucherPromotion = VoucherPromotion.builder()
                .bulkCodePromotionInfos(List.of(
                    BulkCodePromotionInfo.builder().codePrefix("PROMO").codeValue("123").isUsed(true).build()
                ))
                .build();

        ApplyPromotionInfo applyPromotionInfo = ApplyPromotionInfo.builder()
                .voucherCode("PROMO123")
                .build();

        boolean result = bulkCodeVoucherService.isApplicable(voucherPromotion, applyPromotionInfo);
        assertFalse(result);
    }
    @Test
    public void isApplicable_ValidUnusedCode_ReturnsTrue() {
        VoucherPromotion voucherPromotion = VoucherPromotion.builder()
                .bulkCodePromotionInfos(List.of(
                    BulkCodePromotionInfo.builder().codePrefix("PROMO").codeValue("PROMO123").isUsed(false).build()
                ))
                .build();

        ApplyPromotionInfo applyPromotionInfo = ApplyPromotionInfo.builder()
                .voucherCode("PROMO123")
                .build();

        boolean result = bulkCodeVoucherService.isApplicable(voucherPromotion, applyPromotionInfo);
        assert(result);
    }

    // Test cases for calculateVoucherDiscount
    @Test
    public void calculateVoucherDiscount_ByAmountWithTicketQuantityGreaterThanMax_ReturnsCorrectDiscount() {
        VoucherPromotion voucherPromotion = VoucherPromotion.builder()
                .voucherType(VoucherType.BY_AMOUNT)
                .voucherValueByAmount(10.0)
                .maxAppliedTicketCountPerCode(2)
                .build();

        ApplyPromotionInfo applyPromotionInfo = ApplyPromotionInfo.builder()
                .ticketQuantity(3)
                .originalPrice(100.0)
                .build();

        Double discount = bulkCodeVoucherService.calculateVoucherDiscount(voucherPromotion, applyPromotionInfo);
        assert(discount.equals(20.0));
    }
    @Test
    public void calculateVoucherDiscount_ByAmountWithTicketQuantityLessThanMax_ReturnsCorrectDiscount() {
        VoucherPromotion voucherPromotion = VoucherPromotion.builder()
                .voucherType(VoucherType.BY_AMOUNT)
                .voucherValueByAmount(10.0)
                .maxAppliedTicketCountPerCode(5)
                .build();

        ApplyPromotionInfo applyPromotionInfo = ApplyPromotionInfo.builder()
                .ticketQuantity(3)
                .originalPrice(100.0)
                .build();

        Double discount = bulkCodeVoucherService.calculateVoucherDiscount(voucherPromotion, applyPromotionInfo);
        assert(discount.equals(30.0));
    }
    @Test
    public void calculateVoucherDiscount_ByPercentage_ReturnsCorrectDiscount() {
        VoucherPromotion voucherPromotion = VoucherPromotion.builder()
                .voucherType(VoucherType.BY_PERCENTAGE)
                .voucherValueByPercentage(10.0)
                .build();

        ApplyPromotionInfo applyPromotionInfo = ApplyPromotionInfo.builder()
                .ticketQuantity(3)
                .originalPrice(200.0)
                .build();

        Double discount = bulkCodeVoucherService.calculateVoucherDiscount(voucherPromotion, applyPromotionInfo);
        assert(discount.equals(20.0));
    }
}
