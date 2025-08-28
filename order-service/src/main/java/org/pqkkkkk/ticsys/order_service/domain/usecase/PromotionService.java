package org.pqkkkkk.ticsys.order_service.domain.usecase;

public interface PromotionService {
    public boolean isValidPromotion(Long promotionId);
    public Double applyPromotion();
}
