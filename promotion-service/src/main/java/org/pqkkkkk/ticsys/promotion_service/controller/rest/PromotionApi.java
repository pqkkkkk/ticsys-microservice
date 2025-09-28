package org.pqkkkkk.ticsys.promotion_service.controller.rest;

import java.util.Map;

import org.pqkkkkk.ticsys.promotion_service.controller.rest.dto.DTO.VoucherPromotionDto;
import org.pqkkkkk.ticsys.promotion_service.controller.rest.dto.Filter.PromotionFilter;
import org.pqkkkkk.ticsys.promotion_service.controller.rest.dto.Request.CreateVoucherPromotion;
import org.pqkkkkk.ticsys.promotion_service.controller.rest.dto.Request.GetDiscountAmountRequest;
import org.pqkkkkk.ticsys.promotion_service.controller.rest.dto.Request.UpdateVoucherPromotion;
import org.pqkkkkk.ticsys.promotion_service.controller.rest.dto.Response.ApiResponse;
import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionType;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.ApplyPromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.VoucherPromotion;
import org.pqkkkkk.ticsys.promotion_service.domain.service.PromotionCalculatorService;
import org.pqkkkkk.ticsys.promotion_service.domain.service.PromotionCommandExecutor;
import org.pqkkkkk.ticsys.promotion_service.domain.service.PromotionQueryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RequestMapping("/api/v1/promotion")
@RestController
public class PromotionApi {
    private final Map<VoucherPromotionType, PromotionCommandExecutor> promotionCommandExecutors;
    private final PromotionQueryService promotionQueryService;
    private final PromotionCalculatorService promotionCalculatorService;

    public PromotionApi(@Qualifier("promotionCommandExecutors") Map<VoucherPromotionType, PromotionCommandExecutor> promotionCommandExecutors,
            PromotionQueryService promotionQueryService,
            PromotionCalculatorService promotionCalculatorService) {
        this.promotionCommandExecutors = promotionCommandExecutors;
        this.promotionQueryService = promotionQueryService;
        this.promotionCalculatorService = promotionCalculatorService;
    }

    private PromotionCommandExecutor getExecutor(VoucherPromotionType type){
        PromotionCommandExecutor executor = promotionCommandExecutors.getOrDefault(type, null);

        if(executor == null){
            throw new IllegalArgumentException("Unsupported voucher promotion type: " + type);
        }

        return executor;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VoucherPromotionDto>> createVoucherPromotion(@Valid @RequestBody CreateVoucherPromotion request){
        PromotionCommandExecutor executor = getExecutor(request.type());

        VoucherPromotion voucherPromotion = executor.createVoucherPromotion(request.toEntity());

        VoucherPromotionDto dto = VoucherPromotionDto.fromEntity(voucherPromotion);

        ApiResponse<VoucherPromotionDto> response = new ApiResponse<>(dto, true, HttpStatus.CREATED.value(),
                                        "Voucher promotion created successfully", null);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VoucherPromotionDto>> updateVoucherPromotion(@PathVariable Long id, @Valid @RequestBody UpdateVoucherPromotion request){
        PromotionCommandExecutor executor = getExecutor(request.type());

        VoucherPromotion voucherPromotionToUpdate = request.toEntity();
        voucherPromotionToUpdate.setId(id);
        
        VoucherPromotion voucherPromotion = executor.updateVoucherPromotion(voucherPromotionToUpdate);

        VoucherPromotionDto dto = VoucherPromotionDto.fromEntity(voucherPromotion);

        ApiResponse<VoucherPromotionDto> response = new ApiResponse<>(dto, true, HttpStatus.OK.value(),
                                        "Voucher promotion updated successfully", null);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVoucherPromotion(@PathVariable Long id){
        PromotionCommandExecutor executor = getExecutor(VoucherPromotionType.BULK_CODE);

        boolean deleted = executor.deleteVoucherPromotion(id);

        ApiResponse<Void> response = new ApiResponse<>(null, deleted,
                                        deleted ? HttpStatus.OK.value() : HttpStatus.NOT_FOUND.value(),
                                        deleted ? "Voucher promotion deleted successfully" : "Voucher promotion not found", null);

        return ResponseEntity.status(deleted ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(response);
    }
    @GetMapping
    public ResponseEntity<ApiResponse<Page<VoucherPromotionDto>>> getPromotions(
        @Valid @ModelAttribute PromotionFilter filter
    ) {
        Page<VoucherPromotion> page = promotionQueryService.getPromotions(
            filter.currentPage(), filter.pageSize(), filter.sortBy(), filter.sortDirection(),
            filter.nameKeyword(), 
            filter.voucherType(),
            filter.voucherPromotionType(),
            filter.visibility(),
            filter.eventId(), filter.isActive()
        );

        Page<VoucherPromotionDto> dtoPage = page.map(VoucherPromotionDto::fromEntity);

        ApiResponse<Page<VoucherPromotionDto>> response = new ApiResponse<>(dtoPage, true, HttpStatus.OK.value(),
                                        "Promotions retrieved successfully", null);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VoucherPromotionDto>> getPromotionById(@PathVariable Long id) {
        VoucherPromotion vp = promotionQueryService.getPromotionById(id);

        VoucherPromotionDto dto = VoucherPromotionDto.fromEntity(vp);

        ApiResponse<VoucherPromotionDto> response = new ApiResponse<>(dto, true, HttpStatus.OK.value(),
                                        "Promotion retrieved successfully", null);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/discount-amount")
    public ResponseEntity<ApiResponse<Double>> getDiscountAmount(
            @Valid @ModelAttribute GetDiscountAmountRequest request
    ) {
        ApplyPromotionInfo applyPromotionInfo = request.toEntity();

        Double totalDiscount = promotionCalculatorService.calculateDiscount(applyPromotionInfo);

        ApiResponse<Double> response = new ApiResponse<>(totalDiscount, true, HttpStatus.OK.value(),
                                        "Total discount amount calculated successfully", null);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
