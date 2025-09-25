package org.pqkkkkk.ticsys.promotion_service.controller.grpc;

import org.pqkkkk.ticsys.grpc.promotion.ApplyPromotionRequest;
import org.pqkkkk.ticsys.grpc.promotion.ApplyPromotionResponse;
import org.pqkkkk.ticsys.grpc.promotion.PromotionServiceGrpc.PromotionServiceImplBase;
import org.pqkkkkk.ticsys.promotion_service.domain.entity.ApplyPromotionInfo;
import org.pqkkkkk.ticsys.promotion_service.domain.service.PromotionCalculatorService;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class PromotionServiceGrpcImpl extends PromotionServiceImplBase {
    private final PromotionCalculatorService promotionCalculatorService;

    public PromotionServiceGrpcImpl(PromotionCalculatorService promotionCalculatorService) {
        this.promotionCalculatorService = promotionCalculatorService;
    }
    @Override
    public void applyPromotion(ApplyPromotionRequest request,
            StreamObserver<ApplyPromotionResponse> response){
        
        ApplyPromotionInfo applyPromotionInfo = convertRequestToEntity(request);
        Double discount = promotionCalculatorService.calculateDiscount(applyPromotionInfo);

        ApplyPromotionResponse responseMessage = ApplyPromotionResponse.newBuilder()
                .setMessage("Promotion applied successfully")
                .setSuccess(true)
                .setDiscountAmount(discount)
                .build();

        response.onNext(responseMessage);
        response.onCompleted();
    }
    private ApplyPromotionInfo convertRequestToEntity(ApplyPromotionRequest request) {
        return ApplyPromotionInfo.builder()
                .promotionId(request.getPromotionId())
                .userId(request.getUserId())
                .eventId(request.getEventId())
                .eventDateId(request.getEventDateId())
                .originalPrice(request.getOriginalPrice())
                .voucherCode(request.getVoucherCode())
                .voucherType(request.getVoucherType())
                .ticketQuantity(request.getTicketQuantity())
                .build();
    }
}
