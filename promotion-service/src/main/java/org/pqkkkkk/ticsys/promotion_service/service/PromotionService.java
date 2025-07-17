package org.pqkkkkk.ticsys.promotion_service.service;

import java.util.List;

import org.pqkkkkk.ticsys.promotion_service.dto.PromotionsResponse;
import org.pqkkkkk.ticsys.promotion_service.model.Promotion;
import org.pqkkkkk.ticsys.promotion_service.model.VoucherOfUser;;


public interface PromotionService {
    int CreatePromotion(Promotion promotion);
    int UpdatePromotion(Promotion promotion);
    Promotion GetPromotionById(int id);
    PromotionsResponse GetPromotions(int eventId, String status, String type);
    PromotionsResponse GetReductionInfoOfPromotionsOfEvent(int eventId, int currentPrice);
    PromotionsResponse GetPromotionsWithOrderCount(int eventId, String status, String type);
    List<VoucherOfUser> GetVoucherOfUsers(String userId, int promotionId, String status);
}
