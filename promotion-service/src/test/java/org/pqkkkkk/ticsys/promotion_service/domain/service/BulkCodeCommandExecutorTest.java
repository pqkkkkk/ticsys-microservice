package org.pqkkkkk.ticsys.promotion_service.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionType;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherType;
import org.pqkkkkk.ticsys.promotion_service.domain.dao.PromotionCommandDao;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.BulkCodePromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;
import org.pqkkkkk.ticsys.promotion_service.domain.service.external.EventService;
import org.pqkkkkk.ticsys.promotion_service.domain.service.external.IdentityService;
import org.pqkkkkk.ticsys.promotion_service.domain.service.impl.BulkCodeCommandExecutor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BulkCodeCommandExecutorTest {

    @Mock
    private PromotionCommandDao promotionCommandDao;

    @Mock
    private EventService eventService;

    @Mock
    private IdentityService identityService;

    @Mock
    private PromotionQueryService promotionQueryService;

    @InjectMocks
    private BulkCodeCommandExecutor bulkCodeCommandExecutor;

    private VoucherPromotion validVoucherPromotion;
    private List<BulkCodePromotionInfo> bulkCodePromotionInfos;

    @BeforeEach
    void setUp() {
        bulkCodePromotionInfos = Arrays.asList(
                BulkCodePromotionInfo.builder()
                        .codePrefix("BULK")
                        .codeValue("BULK001")
                        .isUsed(false)
                        .build(),
                BulkCodePromotionInfo.builder()
                        .codePrefix("BULK")
                        .codeValue("BULK002")
                        .isUsed(false)
                        .build(),
                BulkCodePromotionInfo.builder()
                        .codePrefix("BULK")
                        .codeValue("BULK003")
                        .isUsed(false)
                        .build()
        );

        validVoucherPromotion = VoucherPromotion.builder()
                .id(1L)
                .promotionName("Test Bulk Promotion")
                .promotionDescription("Test Description")
                .voucherType(VoucherType.BY_PERCENTAGE)
                .voucherValueByPercentage(25.0)
                .voucherPromotionType(VoucherPromotionType.BULK_CODE)
                .maxAppliedTicketCountPerCode(50)
                .numberOfCodes(3)
                .currentAppliedTicketCount(0)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(30))
                .eventId(1L)
                .eventDateId(1L)
                .createdBy(1L)
                .bulkCodePromotionInfos(bulkCodePromotionInfos)
                .build();
    }

    @Test
    void testGetExecutorType() {
        // When
        VoucherPromotionType result = bulkCodeCommandExecutor.getExecutorType();

        // Then
        assertEquals(VoucherPromotionType.BULK_CODE, result);
    }

    @Test
    void testCreateVoucherPromotion_Success() {
        // Given
        when(eventService.isValidEventDate(anyLong(), anyLong())).thenReturn(true);
        when(identityService.isValidUser(anyLong())).thenReturn(true);
        when(promotionCommandDao.createVoucherPromotion(any(VoucherPromotion.class)))
                .thenReturn(validVoucherPromotion);

        // When
        VoucherPromotion result = bulkCodeCommandExecutor.createVoucherPromotion(validVoucherPromotion);

        // Then
        assertNotNull(result);
        assertEquals(validVoucherPromotion.getId(), result.getId());
        assertEquals(validVoucherPromotion.getPromotionName(), result.getPromotionName());
        // Verify that totalAppliedTicketCount is calculated correctly (maxAppliedTicketCountPerCode * number of codes)
        assertEquals(150, validVoucherPromotion.getTotalAppliedTicketCount()); // 50 * 3

        verify(eventService).isValidEventDate(validVoucherPromotion.getEventId(), validVoucherPromotion.getEventDateId());
        verify(identityService).isValidUser(validVoucherPromotion.getCreatedBy());
        verify(promotionCommandDao).createVoucherPromotion(validVoucherPromotion);
    }

    @Test
    void testCreateVoucherPromotion_ThrowsException_WhenBulkCodePromotionInfosIsNull() {
        // Given
        validVoucherPromotion.setBulkCodePromotionInfos(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> bulkCodeCommandExecutor.createVoucherPromotion(validVoucherPromotion));
        
        assertEquals("Invalid promotion information", exception.getMessage());
        verify(eventService, never()).isValidEventDate(anyLong(), anyLong());
        verify(identityService, never()).isValidUser(anyLong());
        verify(promotionCommandDao, never()).createVoucherPromotion(any(VoucherPromotion.class));
    }

    @Test
    void testCreateVoucherPromotion_ThrowsException_WhenBulkCodePromotionInfosIsEmpty() {
        // Given
        validVoucherPromotion.setBulkCodePromotionInfos(Collections.emptyList());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> bulkCodeCommandExecutor.createVoucherPromotion(validVoucherPromotion));
        
        assertEquals("Invalid promotion information", exception.getMessage());
    }

    @Test
    void testCreateVoucherPromotion_ThrowsException_WhenVoucherPromotionTypeIsInvalid() {
        // Given
        validVoucherPromotion.setVoucherPromotionType(VoucherPromotionType.SINGLE_CODE);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> bulkCodeCommandExecutor.createVoucherPromotion(validVoucherPromotion));
        
        assertEquals("Invalid promotion information", exception.getMessage());
    }

    @Test
    void testCreateVoucherPromotion_ThrowsException_WhenVoucherPromotionTypeIsNull() {
        // Given
        validVoucherPromotion.setVoucherPromotionType(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> bulkCodeCommandExecutor.createVoucherPromotion(validVoucherPromotion));
        
        assertEquals("Invalid promotion information", exception.getMessage());
    }

    @Test
    void testCreateVoucherPromotion_ThrowsException_WhenPercentageValueIsMissing() {
        // Given
        validVoucherPromotion.setVoucherType(VoucherType.BY_PERCENTAGE);
        validVoucherPromotion.setVoucherValueByPercentage(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> bulkCodeCommandExecutor.createVoucherPromotion(validVoucherPromotion));
        
        assertEquals("Invalid promotion information", exception.getMessage());
    }

    @Test
    void testCreateVoucherPromotion_ThrowsException_WhenAmountValueIsMissing() {
        // Given
        validVoucherPromotion.setVoucherType(VoucherType.BY_AMOUNT);
        validVoucherPromotion.setVoucherValueByAmount(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> bulkCodeCommandExecutor.createVoucherPromotion(validVoucherPromotion));
        
        assertEquals("Invalid promotion information", exception.getMessage());
    }

    @Test
    void testCreateVoucherPromotion_ThrowsException_WhenEventDateIsInvalid() {
        // Given
        when(eventService.isValidEventDate(anyLong(), anyLong())).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> bulkCodeCommandExecutor.createVoucherPromotion(validVoucherPromotion));
        
        assertEquals("Invalid event date", exception.getMessage());
        verify(eventService).isValidEventDate(validVoucherPromotion.getEventId(), validVoucherPromotion.getEventDateId());
        verify(identityService, never()).isValidUser(anyLong());
        verify(promotionCommandDao, never()).createVoucherPromotion(any(VoucherPromotion.class));
    }

    @Test
    void testCreateVoucherPromotion_ThrowsException_WhenUserIsInvalid() {
        // Given
        when(eventService.isValidEventDate(anyLong(), anyLong())).thenReturn(true);
        when(identityService.isValidUser(anyLong())).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> bulkCodeCommandExecutor.createVoucherPromotion(validVoucherPromotion));
        
        assertEquals("Invalid user", exception.getMessage());
        verify(eventService).isValidEventDate(validVoucherPromotion.getEventId(), validVoucherPromotion.getEventDateId());
        verify(identityService).isValidUser(validVoucherPromotion.getCreatedBy());
        verify(promotionCommandDao, never()).createVoucherPromotion(any(VoucherPromotion.class));
    }

    @Test
    void testCreateVoucherPromotion_Success_WithAmountVoucher() {
        // Given
        validVoucherPromotion.setVoucherType(VoucherType.BY_AMOUNT);
        validVoucherPromotion.setVoucherValueByAmount(75.0);
        validVoucherPromotion.setVoucherValueByPercentage(null);

        when(eventService.isValidEventDate(anyLong(), anyLong())).thenReturn(true);
        when(identityService.isValidUser(anyLong())).thenReturn(true);
        when(promotionCommandDao.createVoucherPromotion(any(VoucherPromotion.class)))
                .thenReturn(validVoucherPromotion);

        // When
        VoucherPromotion result = bulkCodeCommandExecutor.createVoucherPromotion(validVoucherPromotion);

        // Then
        assertNotNull(result);
        assertEquals(VoucherType.BY_AMOUNT, result.getVoucherType());
        assertEquals(75.0, result.getVoucherValueByAmount());
        assertEquals(150, result.getTotalAppliedTicketCount()); // 50 * 3
        
        verify(promotionCommandDao).createVoucherPromotion(validVoucherPromotion);
    }

    @Test
    void testCreateVoucherPromotion_CalculatesTotalAppliedTicketCountCorrectly() {
        // Given
        validVoucherPromotion.setMaxAppliedTicketCountPerCode(100);
        // 5 bulk codes
        List<BulkCodePromotionInfo> fiveCodes = Arrays.asList(
                BulkCodePromotionInfo.builder().codeValue("CODE1").build(),
                BulkCodePromotionInfo.builder().codeValue("CODE2").build(),
                BulkCodePromotionInfo.builder().codeValue("CODE3").build(),
                BulkCodePromotionInfo.builder().codeValue("CODE4").build(),
                BulkCodePromotionInfo.builder().codeValue("CODE5").build()
        );
        validVoucherPromotion.setBulkCodePromotionInfos(fiveCodes);

        when(eventService.isValidEventDate(anyLong(), anyLong())).thenReturn(true);
        when(identityService.isValidUser(anyLong())).thenReturn(true);
        when(promotionCommandDao.createVoucherPromotion(any(VoucherPromotion.class)))
                .thenReturn(validVoucherPromotion);

        // When
        bulkCodeCommandExecutor.createVoucherPromotion(validVoucherPromotion);

        // Then
        assertEquals(500, validVoucherPromotion.getTotalAppliedTicketCount()); // 100 * 5
    }

    @Test
    void testUpdateVoucherPromotion_Success() {
        // Given
        VoucherPromotion existingPromotion = VoucherPromotion.builder()
                .id(1L)
                .promotionName("Old Bulk Name")
                .voucherType(VoucherType.BY_PERCENTAGE)
                .voucherValueByPercentage(15.0)
                .voucherPromotionType(VoucherPromotionType.BULK_CODE)
                .maxAppliedTicketCountPerCode(30)
                .eventId(1L)
                .eventDateId(1L)
                .createdBy(1L)
                .bulkCodePromotionInfos(bulkCodePromotionInfos)
                .build();

        VoucherPromotion updateInfo = VoucherPromotion.builder()
                .id(1L)
                .promotionName("Updated Bulk Name")
                .promotionDescription("Updated Description")
                .eventId(1L)
                .eventDateId(1L)
                .createdBy(1L)
                .updatedBy(2L)
                .build();

        when(promotionQueryService.isValidPromotion(1L)).thenReturn(true);
        when(promotionQueryService.getPromotionById(1L)).thenReturn(existingPromotion);
        when(eventService.isValidEventDate(anyLong(), anyLong())).thenReturn(true);
        when(identityService.isValidUser(anyLong())).thenReturn(true);
        when(promotionCommandDao.updateVoucherPromotion(any(VoucherPromotion.class)))
                .thenReturn(existingPromotion);

        // When
        VoucherPromotion result = bulkCodeCommandExecutor.updateVoucherPromotion(updateInfo);

        // Then
        assertNotNull(result);
        verify(promotionQueryService).isValidPromotion(1L);
        verify(promotionQueryService).getPromotionById(1L);
        verify(eventService).isValidEventDate(updateInfo.getEventId(), updateInfo.getEventDateId());
        verify(identityService).isValidUser(updateInfo.getCreatedBy());
        verify(promotionCommandDao).updateVoucherPromotion(updateInfo);
    }

    @Test
    void testUpdateVoucherPromotion_ThrowsException_WhenPromotionIsNotValid() {
        // Given
        VoucherPromotion updateInfo = VoucherPromotion.builder()
                .id(1L)
                .build();

        when(promotionQueryService.isValidPromotion(1L)).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> bulkCodeCommandExecutor.updateVoucherPromotion(updateInfo));
        
        assertEquals("Promotion is not valid", exception.getMessage());
        verify(promotionQueryService).isValidPromotion(1L);
        verify(promotionQueryService, never()).getPromotionById(anyLong());
        verify(promotionCommandDao, never()).updateVoucherPromotion(any(VoucherPromotion.class));
    }

    @Test
    void testUpdateVoucherPromotion_ThrowsException_WhenEventDateIsInvalid() {
        // Given
        VoucherPromotion existingPromotion = VoucherPromotion.builder()
                .id(1L)
                .voucherType(VoucherType.BY_PERCENTAGE)
                .voucherValueByPercentage(20.0)
                .voucherPromotionType(VoucherPromotionType.BULK_CODE)
                .bulkCodePromotionInfos(bulkCodePromotionInfos)
                .build();

        VoucherPromotion updateInfo = VoucherPromotion.builder()
                .id(1L)
                .eventId(1L)
                .eventDateId(1L)
                .createdBy(1L)
                .build();

        when(promotionQueryService.isValidPromotion(1L)).thenReturn(true);
        when(promotionQueryService.getPromotionById(1L)).thenReturn(existingPromotion);
        when(eventService.isValidEventDate(anyLong(), anyLong())).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> bulkCodeCommandExecutor.updateVoucherPromotion(updateInfo));
        
        assertEquals("Invalid event date", exception.getMessage());
        verify(eventService).isValidEventDate(updateInfo.getEventId(), updateInfo.getEventDateId());
        verify(identityService, never()).isValidUser(anyLong());
        verify(promotionCommandDao, never()).updateVoucherPromotion(any(VoucherPromotion.class));
    }

    @Test
    void testUpdateVoucherPromotion_ThrowsException_WhenUserIsInvalid() {
        // Given
        VoucherPromotion existingPromotion = VoucherPromotion.builder()
                .id(1L)
                .voucherType(VoucherType.BY_PERCENTAGE)
                .voucherValueByPercentage(20.0)
                .voucherPromotionType(VoucherPromotionType.BULK_CODE)
                .bulkCodePromotionInfos(bulkCodePromotionInfos)
                .build();

        VoucherPromotion updateInfo = VoucherPromotion.builder()
                .id(1L)
                .eventId(1L)
                .eventDateId(1L)
                .createdBy(1L)
                .build();

        when(promotionQueryService.isValidPromotion(1L)).thenReturn(true);
        when(promotionQueryService.getPromotionById(1L)).thenReturn(existingPromotion);
        when(eventService.isValidEventDate(anyLong(), anyLong())).thenReturn(true);
        when(identityService.isValidUser(anyLong())).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> bulkCodeCommandExecutor.updateVoucherPromotion(updateInfo));
        
        assertEquals("Invalid user", exception.getMessage());
        verify(eventService).isValidEventDate(updateInfo.getEventId(), updateInfo.getEventDateId());
        verify(identityService).isValidUser(updateInfo.getCreatedBy());
        verify(promotionCommandDao, never()).updateVoucherPromotion(any(VoucherPromotion.class));
    }

    @Test
    void testUpdateVoucherPromotion_UpdatesAllFields() {
        // Given
        VoucherPromotion existingPromotion = VoucherPromotion.builder()
                .id(1L)
                .promotionName("Old Bulk Name")
                .promotionDescription("Old Description")
                .voucherType(VoucherType.BY_PERCENTAGE)
                .voucherValueByPercentage(10.0)
                .voucherPromotionType(VoucherPromotionType.BULK_CODE)
                .maxAppliedTicketCountPerCode(30)
                .eventId(1L)
                .eventDateId(1L)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(10))
                .bulkCodePromotionInfos(bulkCodePromotionInfos)
                .build();

        List<BulkCodePromotionInfo> newBulkCodes = Arrays.asList(
                BulkCodePromotionInfo.builder().codeValue("NEWBULK001").build(),
                BulkCodePromotionInfo.builder().codeValue("NEWBULK002").build()
        );

        VoucherPromotion updateInfo = VoucherPromotion.builder()
                .id(1L)
                .promotionName("New Bulk Name")
                .promotionDescription("New Description")
                .voucherType(VoucherType.BY_AMOUNT)
                .voucherValueByAmount(200.0)
                .maxAppliedTicketCountPerCode(75)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(30))
                .eventId(2L)
                .eventDateId(2L)
                .createdBy(1L)
                .updatedBy(2L)
                .bulkCodePromotionInfos(newBulkCodes)
                .build();

        when(promotionQueryService.isValidPromotion(1L)).thenReturn(true);
        when(promotionQueryService.getPromotionById(1L)).thenReturn(existingPromotion);
        when(eventService.isValidEventDate(anyLong(), anyLong())).thenReturn(true);
        when(identityService.isValidUser(anyLong())).thenReturn(true);
        when(promotionCommandDao.updateVoucherPromotion(any(VoucherPromotion.class)))
                .thenReturn(existingPromotion);

        // When
        bulkCodeCommandExecutor.updateVoucherPromotion(updateInfo);

        // Then
        assertEquals("New Bulk Name", existingPromotion.getPromotionName());
        assertEquals("New Description", existingPromotion.getPromotionDescription());
        assertEquals(VoucherType.BY_AMOUNT, existingPromotion.getVoucherType());
        assertEquals(200.0, existingPromotion.getVoucherValueByAmount());
        assertEquals(2L, existingPromotion.getEventId());
        assertEquals(2L, existingPromotion.getEventDateId());
        assertEquals(2L, existingPromotion.getUpdatedBy());
        assertEquals(newBulkCodes, existingPromotion.getBulkCodePromotionInfos());
        // Check that total applied ticket count is recalculated
        assertEquals(150, existingPromotion.getTotalAppliedTicketCount()); // 75 * 2

        verify(promotionCommandDao).updateVoucherPromotion(updateInfo);
    }

    @Test
    void testUpdateVoucherPromotion_UpdatesTotalAppliedTicketCountWhenBulkCodesChange() {
        // Given
        VoucherPromotion existingPromotion = VoucherPromotion.builder()
                .id(1L)
                .voucherType(VoucherType.BY_PERCENTAGE)
                .voucherValueByPercentage(20.0)
                .voucherPromotionType(VoucherPromotionType.BULK_CODE)
                .maxAppliedTicketCountPerCode(40)
                .totalAppliedTicketCount(120) // 40 * 3 (old calculation)
                .bulkCodePromotionInfos(bulkCodePromotionInfos)
                .build();

        // New bulk codes - different size
        List<BulkCodePromotionInfo> newBulkCodes = Arrays.asList(
                BulkCodePromotionInfo.builder().codeValue("NEW001").build(),
                BulkCodePromotionInfo.builder().codeValue("NEW002").build(),
                BulkCodePromotionInfo.builder().codeValue("NEW003").build(),
                BulkCodePromotionInfo.builder().codeValue("NEW004").build(),
                BulkCodePromotionInfo.builder().codeValue("NEW005").build()
        );

        VoucherPromotion updateInfo = VoucherPromotion.builder()
                .id(1L)
                .maxAppliedTicketCountPerCode(60) // Updated max per code
                .createdBy(1L)
                .eventId(1L)
                .eventDateId(1L)
                .bulkCodePromotionInfos(newBulkCodes)
                .build();

        when(promotionQueryService.isValidPromotion(1L)).thenReturn(true);
        when(promotionQueryService.getPromotionById(1L)).thenReturn(existingPromotion);
        when(eventService.isValidEventDate(anyLong(), anyLong())).thenReturn(true);
        when(identityService.isValidUser(anyLong())).thenReturn(true);
        when(promotionCommandDao.updateVoucherPromotion(any(VoucherPromotion.class)))
                .thenReturn(existingPromotion);

        // When
        bulkCodeCommandExecutor.updateVoucherPromotion(updateInfo);

        // Then
        assertEquals(300, existingPromotion.getTotalAppliedTicketCount()); // 60 * 5
        assertEquals(newBulkCodes, existingPromotion.getBulkCodePromotionInfos());
    }
}
