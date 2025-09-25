package org.pqkkkkk.ticsys.promotion_service.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionType;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionVisibility;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherType;
import org.pqkkkkk.ticsys.promotion_service.domain.dao.PromotionCommandDao;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.ApplyPromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.BulkCodePromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.SingleCodePromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class VoucherDiscountCalculatorTest {
    
    @Autowired
    private PromotionCalculatorService voucherDiscountCalculator;
    
    @Autowired
    private PromotionQueryService promotionQueryService;
    
    @Autowired
    @Qualifier("singleCodeCommandExecutor")
    private PromotionCommandExecutor singleCodeCommandExecutor;
    
    @Autowired
    @Qualifier("bulkCodeCommandExecutor")
    private PromotionCommandExecutor bulkCodeCommandExecutor;

    @Autowired
    private PromotionCommandDao promotionCommandDao;
    
    private VoucherPromotion singleCodePercentagePromotion;
    private VoucherPromotion singleCodeAmountPromotion;
    private VoucherPromotion bulkCodePercentagePromotion;
    private VoucherPromotion bulkCodeAmountPromotion;
    private VoucherPromotion expiredPromotion;
    
    @BeforeEach
    void setUp() {
        setupSingleCodePercentagePromotion();
        setupSingleCodeAmountPromotion();
        setupBulkCodePercentagePromotion();
        setupBulkCodeAmountPromotion();
        setupExpiredPromotion();
    }
    
    private void setupSingleCodePercentagePromotion() {
        SingleCodePromotionInfo singleCodeInfo = SingleCodePromotionInfo.builder()
                .codeValue("SINGLE20")
                .maxAppliedOrderPerUser(1)
                .minTicketQuantityPerOrderToApply(1)
                .maxTicketQuantityPerOrderToApply(5)
                .build();
        
        singleCodePercentagePromotion = VoucherPromotion.builder()
                .promotionName("Single Code 20% Off")
                .promotionDescription("20% discount with single code")
                .voucherType(VoucherType.BY_PERCENTAGE)
                .voucherValueByPercentage(20.0)
                .voucherPromotionType(VoucherPromotionType.SINGLE_CODE)
                .totalAppliedTicketCount(100)
                .currentAppliedTicketCount(0)
                .maxAppliedTicketCountPerCode(100)
                .promotionVisibility(VoucherPromotionVisibility.PUBLIC)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(30))
                .eventId(1L)
                .eventDateId(1L)
                .createdBy(1L)
                .singleCodePromotionInfo(singleCodeInfo)
                .build();
        
        singleCodeInfo.setVoucherPromotion(singleCodePercentagePromotion);
        singleCodePercentagePromotion = promotionCommandDao.createVoucherPromotion(singleCodePercentagePromotion);
    }
    
    private void setupSingleCodeAmountPromotion() {
        SingleCodePromotionInfo singleCodeInfo = SingleCodePromotionInfo.builder()
                .codeValue("SINGLE50")
                .maxAppliedOrderPerUser(1)
                .minTicketQuantityPerOrderToApply(2)
                .maxTicketQuantityPerOrderToApply(10)
                .build();
        
        singleCodeAmountPromotion = VoucherPromotion.builder()
                .promotionName("Single Code $50 Off")
                .promotionDescription("$50 discount per ticket with single code")
                .voucherType(VoucherType.BY_AMOUNT)
                .voucherValueByAmount(50.0)
                .voucherPromotionType(VoucherPromotionType.SINGLE_CODE)
                .totalAppliedTicketCount(50)
                .currentAppliedTicketCount(0)
                .maxAppliedTicketCountPerCode(50)
                .promotionVisibility(VoucherPromotionVisibility.PUBLIC)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(30))
                .eventId(2L)
                .eventDateId(2L)
                .createdBy(1L)
                .singleCodePromotionInfo(singleCodeInfo)
                .build();
        
        singleCodeInfo.setVoucherPromotion(singleCodeAmountPromotion);
        singleCodeAmountPromotion = promotionCommandDao.createVoucherPromotion(singleCodeAmountPromotion);
    }
    
    private void setupBulkCodePercentagePromotion() {
        BulkCodePromotionInfo bulkCode1 = BulkCodePromotionInfo.builder()
                .codePrefix("BULK15")
                .codeValue("ABCD1234")
                .isUsed(false)
                .build();
        
        BulkCodePromotionInfo bulkCode2 = BulkCodePromotionInfo.builder()
                .codePrefix("BULK15")
                .codeValue("EFGH5678")
                .isUsed(false)
                .build();
        
        bulkCodePercentagePromotion = VoucherPromotion.builder()
                .promotionName("Bulk Code 15% Off")
                .promotionDescription("15% discount with bulk codes")
                .voucherType(VoucherType.BY_PERCENTAGE)
                .voucherValueByPercentage(15.0)
                .voucherPromotionType(VoucherPromotionType.BULK_CODE)
                .totalAppliedTicketCount(200)
                .currentAppliedTicketCount(0)
                .maxAppliedTicketCountPerCode(100)
                .numberOfCodes(2)
                .promotionVisibility(VoucherPromotionVisibility.PUBLIC)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(30))
                .eventId(3L)
                .eventDateId(3L)
                .createdBy(1L)
                .bulkCodePromotionInfos(Arrays.asList(bulkCode1, bulkCode2))
                .build();
        
        bulkCode1.setVoucherPromotion(bulkCodePercentagePromotion);
        bulkCode2.setVoucherPromotion(bulkCodePercentagePromotion);
        bulkCodePercentagePromotion = promotionCommandDao.createVoucherPromotion(bulkCodePercentagePromotion);
    }
    
    private void setupBulkCodeAmountPromotion() {
        BulkCodePromotionInfo bulkCode1 = BulkCodePromotionInfo.builder()
                .codePrefix("BULK25")
                .codeValue("QWER9012")
                .isUsed(false)
                .build();
        
        BulkCodePromotionInfo bulkCode2 = BulkCodePromotionInfo.builder()
                .codePrefix("BULK25")
                .codeValue("TYUI3456")
                .isUsed(false)
                .build();
        
        bulkCodeAmountPromotion = VoucherPromotion.builder()
                .promotionName("Bulk Code $25 Off")
                .promotionDescription("$25 discount per ticket with bulk codes")
                .voucherType(VoucherType.BY_AMOUNT)
                .voucherValueByAmount(25.0)
                .voucherPromotionType(VoucherPromotionType.BULK_CODE)
                .totalAppliedTicketCount(100)
                .currentAppliedTicketCount(0)
                .maxAppliedTicketCountPerCode(50)
                .numberOfCodes(2)
                .promotionVisibility(VoucherPromotionVisibility.PUBLIC)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(30))
                .eventId(4L)
                .eventDateId(4L)
                .createdBy(1L)
                .bulkCodePromotionInfos(Arrays.asList(bulkCode1, bulkCode2))
                .build();
        
        bulkCode1.setVoucherPromotion(bulkCodeAmountPromotion);
        bulkCode2.setVoucherPromotion(bulkCodeAmountPromotion);
        bulkCodeAmountPromotion = promotionCommandDao.createVoucherPromotion(bulkCodeAmountPromotion);
    }
    
    private void setupExpiredPromotion() {
        SingleCodePromotionInfo singleCodeInfo = SingleCodePromotionInfo.builder()
                .codeValue("EXPIRED")
                .maxAppliedOrderPerUser(1)
                .minTicketQuantityPerOrderToApply(1)
                .maxTicketQuantityPerOrderToApply(5)
                .build();
        
        expiredPromotion = VoucherPromotion.builder()
                .promotionName("Expired Promotion")
                .promotionDescription("This promotion has expired")
                .voucherType(VoucherType.BY_PERCENTAGE)
                .voucherValueByPercentage(30.0)
                .voucherPromotionType(VoucherPromotionType.SINGLE_CODE)
                .totalAppliedTicketCount(100)
                .currentAppliedTicketCount(0)
                .maxAppliedTicketCountPerCode(100)
                .promotionVisibility(VoucherPromotionVisibility.PUBLIC)
                .startDate(LocalDateTime.now().minusDays(10))
                .endDate(LocalDateTime.now().minusDays(1))
                .eventId(5L)
                .eventDateId(5L)
                .createdBy(1L)
                .singleCodePromotionInfo(singleCodeInfo)
                .build();
        
        singleCodeInfo.setVoucherPromotion(expiredPromotion);
        expiredPromotion = promotionCommandDao.createVoucherPromotion(expiredPromotion);
    }
    
    @Test
    void testCalculateDiscount_SingleCodePercentage_Success() {
        // Given
        ApplyPromotionInfo applyInfo = ApplyPromotionInfo.builder()
                .promotionId(singleCodePercentagePromotion.getId())
                .userId(1L)
                .eventId(1L)
                .eventDateId(1L)
                .originalPrice(1000.0)
                .voucherCode("SINGLE20")
                .voucherType("SINGLE_CODE")
                .ticketQuantity(3)
                .build();
        
        // When
        Double discount = voucherDiscountCalculator.calculateDiscount(applyInfo);
        
        // Then
        assertNotNull(discount);
        assertEquals(200.0, discount, 0.01); // 20% of 1000 = 200
        
        // Verify that promotion state is updated
        VoucherPromotion updatedPromotion = promotionQueryService.getPromotionById(singleCodePercentagePromotion.getId());
        assertEquals(1, updatedPromotion.getCurrentAppliedTicketCount());
    }
    
    @Test
    void testCalculateDiscount_SingleCodeAmount_Success() {
        // Given
        ApplyPromotionInfo applyInfo = ApplyPromotionInfo.builder()
                .promotionId(singleCodeAmountPromotion.getId())
                .userId(1L)
                .eventId(2L)
                .eventDateId(2L)
                .originalPrice(1000.0)
                .voucherCode("SINGLE50")
                .voucherType("SINGLE_CODE")
                .ticketQuantity(3)
                .build();
        
        // When
        Double discount = voucherDiscountCalculator.calculateDiscount(applyInfo);
        
        // Then
        assertNotNull(discount);
        assertEquals(150.0, discount, 0.01); // $50 * 3 tickets = $150
    }
    
    @Test
    void testCalculateDiscount_SingleCodeAmount_WithMaxTicketLimit() {
        // Given
        ApplyPromotionInfo applyInfo = ApplyPromotionInfo.builder()
                .promotionId(singleCodeAmountPromotion.getId())
                .userId(1L)
                .eventId(2L)
                .eventDateId(2L)
                .originalPrice(1000.0)
                .voucherCode("SINGLE50")
                .voucherType("SINGLE_CODE")
                .ticketQuantity(15) // More than max allowed (10)
                .build();
        
        // When
        Double discount = voucherDiscountCalculator.calculateDiscount(applyInfo);
        
        // Then
        assertNotNull(discount);
        assertEquals(500.0, discount, 0.01); // $50 * 10 tickets (max) = $500
    }
    
    @Test
    void testCalculateDiscount_BulkCodePercentage_Success() {
        // Given
        ApplyPromotionInfo applyInfo = ApplyPromotionInfo.builder()
                .promotionId(bulkCodePercentagePromotion.getId())
                .userId(1L)
                .eventId(3L)
                .eventDateId(3L)
                .originalPrice(2000.0)
                .voucherCode("BULK15ABCD1234")
                .voucherType("BULK_CODE")
                .ticketQuantity(5)
                .build();
        
        // When
        Double discount = voucherDiscountCalculator.calculateDiscount(applyInfo);
        
        // Then
        assertNotNull(discount);
        assertEquals(300.0, discount, 0.01); // 15% of 2000 = 300
    }
    
    @Test
    void testCalculateDiscount_BulkCodeAmount_Success() {
        // Given
        ApplyPromotionInfo applyInfo = ApplyPromotionInfo.builder()
                .promotionId(bulkCodeAmountPromotion.getId())
                .userId(1L)
                .eventId(4L)
                .eventDateId(4L)
                .originalPrice(2000.0)
                .voucherCode("BULK25QWER9012")
                .voucherType("BULK_CODE")
                .ticketQuantity(8)
                .build();
        
        // When
        Double discount = voucherDiscountCalculator.calculateDiscount(applyInfo);
        
        // Then
        assertNotNull(discount);
        assertEquals(200.0, discount, 0.01); // $25 * 8 tickets = $200
    }
    
    @Test
    void testCalculateDiscount_BulkCodeAmount_WithMaxTicketLimit() {
        // Given
        ApplyPromotionInfo applyInfo = ApplyPromotionInfo.builder()
                .promotionId(bulkCodeAmountPromotion.getId())
                .userId(1L)
                .eventId(4L)
                .eventDateId(4L)
                .originalPrice(2000.0)
                .voucherCode("BULK25QWER9012")
                .voucherType("BULK_CODE")
                .ticketQuantity(60) // More than max allowed (50)
                .build();
        
        // When
        Double discount = voucherDiscountCalculator.calculateDiscount(applyInfo);
        
        // Then
        assertNotNull(discount);
        assertEquals(1250.0, discount, 0.01); // $25 * 50 tickets (max) = $1250
    }
    
    @Test
    void testCalculateDiscount_InvalidPromotionId_ThrowsException() {
        // Given
        ApplyPromotionInfo applyInfo = ApplyPromotionInfo.builder()
                .promotionId(999L) // Non-existent promotion
                .userId(1L)
                .eventId(1L)
                .eventDateId(1L)
                .originalPrice(1000.0)
                .voucherCode("SINGLE20")
                .voucherType("SINGLE_CODE")
                .ticketQuantity(3)
                .build();
        
        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> voucherDiscountCalculator.calculateDiscount(applyInfo));
    }
    
    @Test
    void testCalculateDiscount_ExpiredPromotion_ThrowsException() {
        // Given
        ApplyPromotionInfo applyInfo = ApplyPromotionInfo.builder()
                .promotionId(expiredPromotion.getId())
                .userId(1L)
                .eventId(5L)
                .eventDateId(5L)
                .originalPrice(1000.0)
                .voucherCode("EXPIRED")
                .voucherType("SINGLE_CODE")
                .ticketQuantity(3)
                .build();
        
        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> voucherDiscountCalculator.calculateDiscount(applyInfo));;
    }
    
    @Test
    void testCalculateDiscount_InvalidVoucherCode_ThrowsException() {
        // Given
        ApplyPromotionInfo applyInfo = ApplyPromotionInfo.builder()
                .promotionId(singleCodePercentagePromotion.getId())
                .userId(1L)
                .eventId(1L)
                .eventDateId(1L)
                .originalPrice(1000.0)
                .voucherCode("INVALID_CODE")
                .voucherType("SINGLE_CODE")
                .ticketQuantity(3)
                .build();
        
        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> voucherDiscountCalculator.calculateDiscount(applyInfo));
        
        assertEquals("Voucher is not valid for the given conditions", exception.getMessage());
    }
    
    @Test
    void testCalculateDiscount_InsufficientTicketQuantity_ThrowsException() {
        // Given
        ApplyPromotionInfo applyInfo = ApplyPromotionInfo.builder()
                .promotionId(singleCodeAmountPromotion.getId())
                .userId(1L)
                .eventId(2L)
                .eventDateId(2L)
                .originalPrice(1000.0)
                .voucherCode("SINGLE50")
                .voucherType("SINGLE_CODE")
                .ticketQuantity(1) // Less than minimum required (2)
                .build();
        
        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> voucherDiscountCalculator.calculateDiscount(applyInfo));
        
        assertEquals("Voucher is not valid for the given conditions", exception.getMessage());
    }
    
    @Test
    void testCalculateDiscount_UsedBulkCode_ThrowsException() {
        // Given - First use the bulk code
        ApplyPromotionInfo firstUseInfo = ApplyPromotionInfo.builder()
                .promotionId(bulkCodeAmountPromotion.getId())
                .userId(1L)
                .eventId(4L)
                .eventDateId(4L)
                .originalPrice(1000.0)
                .voucherCode("BULK25QWER9012")
                .voucherType("BULK_CODE")
                .ticketQuantity(3)
                .build();
        
        voucherDiscountCalculator.calculateDiscount(firstUseInfo);
        
        // Given - Try to use the same bulk code again
        ApplyPromotionInfo secondUseInfo = ApplyPromotionInfo.builder()
                .promotionId(bulkCodeAmountPromotion.getId())
                .userId(2L)
                .eventId(4L)
                .eventDateId(4L)
                .originalPrice(1000.0)
                .voucherCode("BULK25QWER9012")
                .voucherType("BULK_CODE")
                .ticketQuantity(3)
                .build();
        
        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> voucherDiscountCalculator.calculateDiscount(secondUseInfo));
        
        assertEquals("Voucher is not valid for the given conditions", exception.getMessage());
    }
    
    @Test
    void testCalculateDiscount_ExhaustedSingleCodePromotion_ThrowsException() {
        // Given - Set the promotion to exhausted state
        singleCodePercentagePromotion.setCurrentAppliedTicketCount(100); // Same as max
        singleCodeCommandExecutor.updateVoucherPromotion(singleCodePercentagePromotion);
        
        ApplyPromotionInfo applyInfo = ApplyPromotionInfo.builder()
                .promotionId(singleCodePercentagePromotion.getId())
                .userId(1L)
                .eventId(1L)
                .eventDateId(1L)
                .originalPrice(1000.0)
                .voucherCode("SINGLE20")
                .voucherType("SINGLE_CODE")
                .ticketQuantity(3)
                .build();
        
        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> voucherDiscountCalculator.calculateDiscount(applyInfo));
        
        assertEquals("Voucher is not valid for the given conditions", exception.getMessage());
    }
    
    @Test
    void testCalculateDiscount_MultipleApplications_UpdatesCountCorrectly() {
        // Given - Apply first discount
        ApplyPromotionInfo firstApplyInfo = ApplyPromotionInfo.builder()
                .promotionId(singleCodePercentagePromotion.getId())
                .userId(1L)
                .eventId(1L)
                .eventDateId(1L)
                .originalPrice(1000.0)
                .voucherCode("SINGLE20")
                .voucherType("SINGLE_CODE")
                .ticketQuantity(3)
                .build();
        
        // When
        Double firstDiscount = voucherDiscountCalculator.calculateDiscount(firstApplyInfo);
        
        // Then
        assertNotNull(firstDiscount);
        assertEquals(200.0, firstDiscount, 0.01);
        
        // Given - Apply second discount
        ApplyPromotionInfo secondApplyInfo = ApplyPromotionInfo.builder()
                .promotionId(singleCodePercentagePromotion.getId())
                .userId(2L)
                .eventId(1L)
                .eventDateId(1L)
                .originalPrice(500.0)
                .voucherCode("SINGLE20")
                .voucherType("SINGLE_CODE")
                .ticketQuantity(2)
                .build();
        
        // When
        Double secondDiscount = voucherDiscountCalculator.calculateDiscount(secondApplyInfo);
        
        // Then
        assertNotNull(secondDiscount);
        assertEquals(100.0, secondDiscount, 0.01);
        
        // Verify total count is updated
        VoucherPromotion updatedPromotion = promotionQueryService.getPromotionById(singleCodePercentagePromotion.getId());
        assertEquals(2, updatedPromotion.getCurrentAppliedTicketCount());
    }
}
