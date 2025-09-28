package org.pqkkkkk.ticsys.order_service.infrastructure.client;

import org.pqkkkk.ticsys.grpc.promotion.ApplyPromotionRequest;
import org.pqkkkk.ticsys.grpc.promotion.PromotionServiceGrpc.PromotionServiceBlockingStub;
import org.pqkkkkk.ticsys.order_service.domain.entity.ApplyPromotionInfo;
import org.pqkkkkk.ticsys.order_service.domain.usecase.PromotionService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
@Profile("!test")
public class PromotionGrpcClient implements PromotionService {
    @GrpcClient("promotion-service")
    private PromotionServiceBlockingStub blockingStub;
    
    @Override
    public Double applyPromotion(ApplyPromotionInfo applyPromotionInfo) {
        try{
            return blockingStub.applyPromotion(toGrpcRequest(applyPromotionInfo)).getDiscountAmount();
        }
        catch(StatusRuntimeException e){
            throw new RuntimeException("gRPC call to Promotion Service failed: " + e.getStatus().getDescription(), e);
        }
    }
    private ApplyPromotionRequest toGrpcRequest(ApplyPromotionInfo applyPromotionInfo){
        return ApplyPromotionRequest.newBuilder()
                .setPromotionId(applyPromotionInfo.getPromotionId())
                .setUserId(applyPromotionInfo.getUserId())
                .setEventId(applyPromotionInfo.getEventId())
                .setEventDateId(applyPromotionInfo.getEventDateId())
                .setTicketQuantity(applyPromotionInfo.getTicketQuantity())
                .setOriginalPrice(applyPromotionInfo.getOriginalPrice())
                .setVoucherCode(applyPromotionInfo.getVoucherCode())
                .build();
    }

}
