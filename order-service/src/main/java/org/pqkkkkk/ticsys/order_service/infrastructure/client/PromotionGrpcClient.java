package org.pqkkkkk.ticsys.order_service.infrastructure.client;

import org.pqkkkk.ticsys.grpc.promotion.PromotionServiceGrpc.PromotionServiceBlockingStub;
import org.pqkkkkk.ticsys.order_service.domain.entity.ApplyPromotionInfo;
import org.pqkkkkk.ticsys.order_service.domain.usecase.PromotionService;
import org.springframework.stereotype.Service;

import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class PromotionGrpcClient implements PromotionService {
    @GrpcClient("promotion-service")
    private final PromotionServiceBlockingStub blockingStub;

    public PromotionGrpcClient(PromotionServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }
    @Override
    public Double applyPromotion(ApplyPromotionInfo applyPromotionInfo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'applyPromotion'");
    }

}
