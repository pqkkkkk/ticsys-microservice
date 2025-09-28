package org.pqkkkkk.ticsys.promotion_service.controller.rest.dto;

import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionType;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionVisibility;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherType;

import jakarta.validation.constraints.Pattern;

public class Filter {
    public record PromotionFilter(
        Integer currentPage,

        Integer pageSize,

        String sortBy,

        @Pattern(regexp = "ASC|DESC", message = "sortDirection must be 'ASC' or 'DESC'")
        String sortDirection,

        String nameKeyword,

        VoucherType voucherType,

        VoucherPromotionType voucherPromotionType,

        VoucherPromotionVisibility visibility,

        Long eventId,

        Boolean isActive
    ){}
}
