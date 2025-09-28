package org.pqkkkkk.ticsys.promotion_service.controller.rest.dto;

import java.time.LocalDateTime;

import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionType;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionVisibility;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherType;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.ApplyPromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.BulkCodePromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.SingleCodePromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Request {
    public record CreateVoucherPromotion(
        VoucherPromotionType type,
        @NotNull(message = "Promotion name cannot be null")
        @NotBlank(message = "Promotion name cannot be blank")
        String promotionName,

        String promotionDescription,

        VoucherType voucherType,

        Double voucherValueByPercentage,

        Double voucherValueByAmount,

        VoucherPromotionType voucherPromotionType,

        Integer numberOfCodes,

        @NotNull(message = "Max applied ticket count per code cannot be null")
        @Min(value = 1, message = "Max applied ticket count per code must be at least 1")
        Integer maxAppliedTicketCountPerCode,

        VoucherPromotionVisibility visibility,

        @NotNull(message = "Start date cannot be null")
        LocalDateTime startDate,
        @NotNull(message = "End date cannot be null")
        LocalDateTime endDate,

        @NotNull(message = "Event ID cannot be null")
        Long eventId,

        @NotNull(message = "Event date ID cannot be null")
        Long eventDateId,

        @NotNull(message = "Creator ID cannot be null")
        Long createdBy,

        CreateSingleCodeVoucherPromotion singleCodePromotionInfo,

        CreateBulkCodeVoucherPromotion bulkCodePromotionInfo

    ){
        public VoucherPromotion toEntity(){
            VoucherPromotion voucherPromotion = VoucherPromotion.builder()
                .promotionName(promotionName)
                .promotionDescription(promotionDescription)
                .voucherType(voucherType)
                .voucherValueByPercentage(voucherValueByPercentage)
                .voucherValueByAmount(voucherValueByAmount)
                .voucherPromotionType(voucherPromotionType)
                .numberOfCodes(numberOfCodes)
                .maxAppliedTicketCountPerCode(maxAppliedTicketCountPerCode)
                .promotionVisibility(visibility)
                .startDate(startDate)
                .endDate(endDate)
                .eventId(eventId)
                .eventDateId(eventDateId)
                .createdBy(createdBy)
                .totalAppliedTicketCount(maxAppliedTicketCountPerCode)
                .singleCodePromotionInfo(singleCodePromotionInfo == null ? null : singleCodePromotionInfo.toEntity())
                .bulkCodePromotionInfos(bulkCodePromotionInfo == null ? null : java.util.List.of(bulkCodePromotionInfo.toEntity()))
            .build();

            if(singleCodePromotionInfo != null){
                voucherPromotion.getSingleCodePromotionInfo().setVoucherPromotion(voucherPromotion);
            }
            if(bulkCodePromotionInfo != null){
                voucherPromotion.getBulkCodePromotionInfos().forEach(info -> info.setVoucherPromotion(voucherPromotion));
            }

            return voucherPromotion;
        }
    }

    public record CreateSingleCodeVoucherPromotion(
        @NotNull(message = "Code value cannot be null")
        @NotBlank(message = "Code value cannot be blank")
        String codeValue,

        @NotNull(message = "Max applied order per user cannot be null")
        @Min(value = 1, message = "Max applied order per user must be at least 1")
        Integer maxAppliedOrderPerUser,
        
        @NotNull(message = "Min ticket quantity per order to apply cannot be null")
        @Min(value = 0, message = "Min ticket quantity per order to apply must be at least 0")
        Integer minTicketQuantityPerOrderToApply,

        @NotNull(message = "Max ticket quantity per order to apply cannot be null")
        @Min(value = 1, message = "Max ticket quantity per order to apply must be at least 1")
        Integer maxTicketQuantityPerOrderToApply
    ){
        public SingleCodePromotionInfo toEntity(){
            return SingleCodePromotionInfo.builder()
                .codeValue(codeValue)
                .maxAppliedOrderPerUser(maxAppliedOrderPerUser)
                .minTicketQuantityPerOrderToApply(minTicketQuantityPerOrderToApply)
                .maxTicketQuantityPerOrderToApply(maxTicketQuantityPerOrderToApply)
                .build();
        }
    }

    public record CreateBulkCodeVoucherPromotion(
        @NotNull(message = "Number of codes cannot be null")
        @NotBlank(message = "Number of codes cannot be blank")
        String codePrefix
    ){
        public BulkCodePromotionInfo toEntity(){
            return BulkCodePromotionInfo.builder()
                .codePrefix(codePrefix)
                .build();
        }
    }

    public record UpdateVoucherPromotion(
        @NotNull(message = "Promotion ID cannot be null")
        Long voucherPromotionId,

        VoucherPromotionType type,

        String promotionName,

        String promotionDescription,

        VoucherType voucherType,

        Double voucherValueByPercentage,

        Double voucherValueByAmount,

        Integer numberOfCodes,

        @Min(value = 1, message = "Max applied ticket count per code must be at least 1")
        Integer maxAppliedTicketCountPerCode,

        VoucherPromotionVisibility visibility,

        LocalDateTime startDate,
        LocalDateTime endDate,

        Long eventDateId,

        @NotNull(message = "Updater ID cannot be null")
        Long updatedBy
    ){
        public VoucherPromotion toEntity(){
            return VoucherPromotion.builder()
                .id(voucherPromotionId)
                .promotionName(promotionName)
                .promotionDescription(promotionDescription)
                .voucherPromotionType(type)
                .voucherType(voucherType)
                .voucherValueByPercentage(voucherValueByPercentage)
                .voucherValueByAmount(voucherValueByAmount)
                .numberOfCodes(numberOfCodes)
                .maxAppliedTicketCountPerCode(maxAppliedTicketCountPerCode)
                .promotionVisibility(visibility)
                .startDate(startDate)
                .endDate(endDate)
                .eventDateId(eventDateId)
                .updatedBy(updatedBy)
            .build();
        }
    }

    public record UpdateSingleCodeVoucherPromotion(
        @NotNull(message = "Code value cannot be null")
        @NotBlank(message = "Code value cannot be blank")
        String codeValue,

        @NotNull(message = "Max applied order per user cannot be null")
        @Min(value = 1, message = "Max applied order per user must be at least 1")
        Integer maxAppliedOrderPerUser,
        
        @NotNull(message = "Min ticket quantity per order to apply cannot be null")
        @Min(value = 0, message = "Min ticket quantity per order to apply must be at least 0")
        Integer minTicketQuantityPerOrderToApply,

        @NotNull(message = "Max ticket quantity per order to apply cannot be null")
        @Min(value = 1, message = "Max ticket quantity per order to apply must be at least 1")
        Integer maxTicketQuantityPerOrderToApply
    ){
        public SingleCodePromotionInfo toEntity(){
            return SingleCodePromotionInfo.builder()
                .codeValue(codeValue)
                .maxAppliedOrderPerUser(maxAppliedOrderPerUser)
                .minTicketQuantityPerOrderToApply(minTicketQuantityPerOrderToApply)
                .maxTicketQuantityPerOrderToApply(maxTicketQuantityPerOrderToApply)
                .build();
        }
    }

    public record UpdateBulkCodeVoucherPromotion(
        @NotNull(message = "Number of codes cannot be null")
        @NotBlank(message = "Number of codes cannot be blank")
        String codePrefix
    ){
        public BulkCodePromotionInfo toEntity(){
            return BulkCodePromotionInfo.builder()
                .codePrefix(codePrefix)
                .build();
        }
    }

    public record GetDiscountAmountRequest(
        @NotNull(message = "Promotion ID cannot be null")
        Long promotionId,

        @NotNull(message = "Ticket quantity cannot be null")
        @Min(value = 1, message = "Ticket quantity must be at least 1")
        Integer ticketQuantity,

        @NotNull(message = "Total amount cannot be null")
        @Min(value = 0, message = "Total amount must be at least 0")
        Double originalPrice,

        @NotNull(message = "Voucher code cannot be null")
        String voucherCode,

        @NotNull(message = "Voucher type cannot be null")
        VoucherType voucherType
    ){
        public ApplyPromotionInfo toEntity(){
            return ApplyPromotionInfo.builder()
                .promotionId(promotionId)
                .ticketQuantity(ticketQuantity)
                .originalPrice(originalPrice)
                .voucherCode(voucherCode)
                .voucherType(voucherType.name())
                .build();
        }
    }
}
