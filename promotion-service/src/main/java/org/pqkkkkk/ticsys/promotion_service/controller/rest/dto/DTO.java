package org.pqkkkkk.ticsys.promotion_service.controller.rest.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionType;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionVisibility;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherType;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.BulkCodePromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.SingleCodePromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;

public class DTO {
    public record VoucherPromotionDto(
        Long id,
        String promotionName,
        String promotionDescription,
        VoucherType voucherType,
        Double voucherValueByPercentage,
        Double voucherValueByAmount,
        VoucherPromotionType voucherPromotionType,
        Integer totalAppliedTicketCount,
        Integer currentAppliedTicketCount,
        Integer maxAppliedTicketCountPerCode,
        Integer numberOfCodes,
        VoucherPromotionVisibility promotionVisibility,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long eventId,
        Long eventDateId,
        Long createdBy,
        SingleCodePromotionInfoDto singleCodePromotionInfo,
        List<BulkCodePromotionInfoDto> bulkCodePromotionInfos
    ){
        public static VoucherPromotionDto fromEntity(VoucherPromotion voucherPromotion){
            VoucherPromotionDto dto = new VoucherPromotionDto(
                voucherPromotion.getId(),
                voucherPromotion.getPromotionName(),
                voucherPromotion.getPromotionDescription(),
                voucherPromotion.getVoucherType(),
                voucherPromotion.getVoucherValueByPercentage(),
                voucherPromotion.getVoucherValueByAmount(),
                voucherPromotion.getVoucherPromotionType(),
                voucherPromotion.getTotalAppliedTicketCount(),
                voucherPromotion.getCurrentAppliedTicketCount(),
                voucherPromotion.getMaxAppliedTicketCountPerCode(),
                voucherPromotion.getNumberOfCodes(),
                voucherPromotion.getPromotionVisibility(),
                voucherPromotion.getStartDate(),
                voucherPromotion.getEndDate(),
                voucherPromotion.getCreatedAt(),
                voucherPromotion.getUpdatedAt(),
                voucherPromotion.getEventId(),
                voucherPromotion.getEventDateId(),
                voucherPromotion.getCreatedBy(),
                voucherPromotion.getSingleCodePromotionInfo() == null ? null : SingleCodePromotionInfoDto.fromEntity(voucherPromotion.getSingleCodePromotionInfo()),
                voucherPromotion.getBulkCodePromotionInfos() == null ? null : voucherPromotion.getBulkCodePromotionInfos().stream()
                    .map(BulkCodePromotionInfoDto::fromEntity)
                    .collect(Collectors.toList())
            );

            return dto;
        }
    }

    public record SingleCodePromotionInfoDto(
        Long scpiId,
        String codeValue,
        Integer maxAppliedOrderPerUser,
        Integer minTicketQuantityPerOrderToApply,
        Integer maxTicketQuantityPerOrderToApply
    ){
        public static SingleCodePromotionInfoDto fromEntity(SingleCodePromotionInfo info) {
            return new SingleCodePromotionInfoDto(
                info.getScpiId(),
                info.getCodeValue(),
                info.getMaxAppliedOrderPerUser(),
                info.getMinTicketQuantityPerOrderToApply(),
                info.getMaxTicketQuantityPerOrderToApply()
            );
        }
    }

    public record BulkCodePromotionInfoDto(
        Long bcpiId,
        String codePrefix,
        String codeValue,
        Boolean isUsed,
        Long usedBy
    ){
        public static BulkCodePromotionInfoDto fromEntity(BulkCodePromotionInfo info) {
            return new BulkCodePromotionInfoDto(
                info.getBcpiId(),
                info.getCodePrefix(),
                info.getCodeValue(),
                info.getIsUsed(),
                info.getUsedBy()
            );
        }
    }
}
