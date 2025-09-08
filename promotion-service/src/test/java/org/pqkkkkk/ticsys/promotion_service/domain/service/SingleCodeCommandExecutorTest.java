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
import org.pqkkkkk.ticsys.promotion_service.domain.entity.SingleCodePromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;
import org.pqkkkkk.ticsys.promotion_service.domain.service.external.EventService;
import org.pqkkkkk.ticsys.promotion_service.domain.service.external.IdentityService;
import org.pqkkkkk.ticsys.promotion_service.domain.service.impl.SingleCodeCommandExecutor;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SingleCodeCommandExecutorTest {

    @Mock
    private PromotionCommandDao promotionCommandDao;

    @Mock
    private EventService eventService;

    @Mock
    private IdentityService identityService;

    @Mock
    private PromotionQueryService promotionQueryService;

    @InjectMocks
    private SingleCodeCommandExecutor singleCodeCommandExecutor;

    private VoucherPromotion validVoucherPromotion;
    private SingleCodePromotionInfo singleCodePromotionInfo;

    @BeforeEach
    void setUp() {
        singleCodePromotionInfo = SingleCodePromotionInfo.builder()
                .codeValue("TEST123")
                .maxAppliedOrderPerUser(5)
                .minTicketQuantityPerOrderToApply(1)
                .maxTicketQuantityPerOrderToApply(10)
                .build();

        validVoucherPromotion = VoucherPromotion.builder()
                .id(1L)
                .promotionName("Test Promotion")
                .promotionDescription("Test Description")
                .voucherType(VoucherType.BY_PERCENTAGE)
                .voucherValueByPercentage(20.0)
                .voucherPromotionType(VoucherPromotionType.SINGLE_CODE)
                .totalAppliedTicketCount(100)
                .currentAppliedTicketCount(0)
                .maxAppliedTicketCountPerCode(100)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(30))
                .eventId(1L)
                .eventDateId(1L)
                .createdBy(1L)
                .singleCodePromotionInfo(singleCodePromotionInfo)
                .build();
    }

    @Test
    void testGetExecutorType() {
        // When
        VoucherPromotionType result = singleCodeCommandExecutor.getExecutorType();

        // Then
        assertEquals(VoucherPromotionType.SINGLE_CODE, result);
    }

    @Test
    void testCreateVoucherPromotion_Success() {
        // Given
        when(eventService.isValidEventDate(anyLong(), anyLong())).thenReturn(true);
        when(identityService.isValidUser(anyLong())).thenReturn(true);
        when(promotionCommandDao.createVoucherPromotion(any(VoucherPromotion.class)))
                .thenReturn(validVoucherPromotion);

        // When
        VoucherPromotion result = singleCodeCommandExecutor.createVoucherPromotion(validVoucherPromotion);

        // Then
        assertNotNull(result);
        assertEquals(validVoucherPromotion.getId(), result.getId());
        assertEquals(validVoucherPromotion.getPromotionName(), result.getPromotionName());

        verify(eventService).isValidEventDate(validVoucherPromotion.getEventId(), validVoucherPromotion.getEventDateId());
        verify(identityService).isValidUser(validVoucherPromotion.getCreatedBy());
        verify(promotionCommandDao).createVoucherPromotion(validVoucherPromotion);
    }

    @Test
    void testCreateVoucherPromotion_ThrowsException_WhenPromotionInfoIsInvalid() {
        // Given - Remove single code promotion info to make it invalid
        validVoucherPromotion.setSingleCodePromotionInfo(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> singleCodeCommandExecutor.createVoucherPromotion(validVoucherPromotion));
        
        assertEquals("Invalid promotion information", exception.getMessage());
        verify(eventService, never()).isValidEventDate(anyLong(), anyLong());
        verify(identityService, never()).isValidUser(anyLong());
        verify(promotionCommandDao, never()).createVoucherPromotion(any(VoucherPromotion.class));
    }

    @Test
    void testCreateVoucherPromotion_ThrowsException_WhenVoucherPromotionTypeIsInvalid() {
        // Given
        validVoucherPromotion.setVoucherPromotionType(VoucherPromotionType.BULK_CODE);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> singleCodeCommandExecutor.createVoucherPromotion(validVoucherPromotion));
        
        assertEquals("Invalid promotion information", exception.getMessage());
    }

    @Test
    void testCreateVoucherPromotion_ThrowsException_WhenVoucherPromotionTypeIsNull() {
        // Given
        validVoucherPromotion.setVoucherPromotionType(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> singleCodeCommandExecutor.createVoucherPromotion(validVoucherPromotion));
        
        assertEquals("Invalid promotion information", exception.getMessage());
    }

    @Test
    void testCreateVoucherPromotion_ThrowsException_WhenPercentageValueIsMissing() {
        // Given
        validVoucherPromotion.setVoucherType(VoucherType.BY_PERCENTAGE);
        validVoucherPromotion.setVoucherValueByPercentage(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> singleCodeCommandExecutor.createVoucherPromotion(validVoucherPromotion));
        
        assertEquals("Invalid promotion information", exception.getMessage());
    }

    @Test
    void testCreateVoucherPromotion_ThrowsException_WhenAmountValueIsMissing() {
        // Given
        validVoucherPromotion.setVoucherType(VoucherType.BY_AMOUNT);
        validVoucherPromotion.setVoucherValueByAmount(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> singleCodeCommandExecutor.createVoucherPromotion(validVoucherPromotion));
        
        assertEquals("Invalid promotion information", exception.getMessage());
    }

    @Test
    void testCreateVoucherPromotion_ThrowsException_WhenEventDateIsInvalid() {
        // Given
        when(eventService.isValidEventDate(anyLong(), anyLong())).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> singleCodeCommandExecutor.createVoucherPromotion(validVoucherPromotion));
        
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
                () -> singleCodeCommandExecutor.createVoucherPromotion(validVoucherPromotion));
        
        assertEquals("Invalid user", exception.getMessage());
        verify(eventService).isValidEventDate(validVoucherPromotion.getEventId(), validVoucherPromotion.getEventDateId());
        verify(identityService).isValidUser(validVoucherPromotion.getCreatedBy());
        verify(promotionCommandDao, never()).createVoucherPromotion(any(VoucherPromotion.class));
    }

    @Test
    void testCreateVoucherPromotion_Success_WithAmountVoucher() {
        // Given
        validVoucherPromotion.setVoucherType(VoucherType.BY_AMOUNT);
        validVoucherPromotion.setVoucherValueByAmount(50.0);
        validVoucherPromotion.setVoucherValueByPercentage(null);

        when(eventService.isValidEventDate(anyLong(), anyLong())).thenReturn(true);
        when(identityService.isValidUser(anyLong())).thenReturn(true);
        when(promotionCommandDao.createVoucherPromotion(any(VoucherPromotion.class)))
                .thenReturn(validVoucherPromotion);

        // When
        VoucherPromotion result = singleCodeCommandExecutor.createVoucherPromotion(validVoucherPromotion);

        // Then
        assertNotNull(result);
        assertEquals(VoucherType.BY_AMOUNT, result.getVoucherType());
        assertEquals(50.0, result.getVoucherValueByAmount());
        
        verify(promotionCommandDao).createVoucherPromotion(validVoucherPromotion);
    }

    @Test
    void testUpdateVoucherPromotion_Success() {
        // Given
        VoucherPromotion existingPromotion = VoucherPromotion.builder()
                .id(1L)
                .promotionName("Old Name")
                .voucherType(VoucherType.BY_PERCENTAGE)
                .voucherValueByPercentage(10.0)
                .voucherPromotionType(VoucherPromotionType.SINGLE_CODE)
                .eventId(1L)
                .eventDateId(1L)
                .createdBy(1L)
                .singleCodePromotionInfo(singleCodePromotionInfo)
                .build();

        VoucherPromotion updateInfo = VoucherPromotion.builder()
                .id(1L)
                .promotionName("Updated Name")
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
        VoucherPromotion result = singleCodeCommandExecutor.updateVoucherPromotion(updateInfo);

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
                () -> singleCodeCommandExecutor.updateVoucherPromotion(updateInfo));
        
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
                .voucherPromotionType(VoucherPromotionType.SINGLE_CODE)
                .singleCodePromotionInfo(singleCodePromotionInfo)
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
                () -> singleCodeCommandExecutor.updateVoucherPromotion(updateInfo));
        
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
                .voucherPromotionType(VoucherPromotionType.SINGLE_CODE)
                .singleCodePromotionInfo(singleCodePromotionInfo)
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
                () -> singleCodeCommandExecutor.updateVoucherPromotion(updateInfo));
        
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
                .promotionName("Old Name")
                .promotionDescription("Old Description")
                .voucherType(VoucherType.BY_PERCENTAGE)
                .voucherValueByPercentage(10.0)
                .voucherPromotionType(VoucherPromotionType.SINGLE_CODE)
                .eventId(1L)
                .eventDateId(1L)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(10))
                .singleCodePromotionInfo(singleCodePromotionInfo)
                .build();

        SingleCodePromotionInfo newSingleCodeInfo = SingleCodePromotionInfo.builder()
                .codeValue("NEWCODE")
                .build();

        VoucherPromotion updateInfo = VoucherPromotion.builder()
                .id(1L)
                .promotionName("New Name")
                .promotionDescription("New Description")
                .voucherType(VoucherType.BY_AMOUNT)
                .voucherValueByAmount(100.0)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(30))
                .eventId(2L)
                .eventDateId(2L)
                .createdBy(1L)
                .updatedBy(2L)
                .singleCodePromotionInfo(newSingleCodeInfo)
                .build();

        when(promotionQueryService.isValidPromotion(1L)).thenReturn(true);
        when(promotionQueryService.getPromotionById(1L)).thenReturn(existingPromotion);
        when(eventService.isValidEventDate(anyLong(), anyLong())).thenReturn(true);
        when(identityService.isValidUser(anyLong())).thenReturn(true);
        when(promotionCommandDao.updateVoucherPromotion(any(VoucherPromotion.class)))
                .thenReturn(existingPromotion);

        // When
        singleCodeCommandExecutor.updateVoucherPromotion(updateInfo);

        // Then
        assertEquals("New Name", existingPromotion.getPromotionName());
        assertEquals("New Description", existingPromotion.getPromotionDescription());
        assertEquals(VoucherType.BY_AMOUNT, existingPromotion.getVoucherType());
        assertEquals(100.0, existingPromotion.getVoucherValueByAmount());
        assertEquals(2L, existingPromotion.getEventId());
        assertEquals(2L, existingPromotion.getEventDateId());
        assertEquals(2L, existingPromotion.getUpdatedBy());
        assertEquals(newSingleCodeInfo, existingPromotion.getSingleCodePromotionInfo());

        verify(promotionCommandDao).updateVoucherPromotion(updateInfo);
    }
}
