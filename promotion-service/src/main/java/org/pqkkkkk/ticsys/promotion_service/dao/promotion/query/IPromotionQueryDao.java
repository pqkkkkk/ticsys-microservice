package org.pqkkkkk.ticsys.promotion_service.dao.promotion.query;

import java.util.List;

import org.pqkkkkk.ticsys.promotion_service.dto.PromotionDto;

public interface IPromotionQueryDao {
    public List<PromotionDto> GetPromotions(int eventId, String status, String type);
    public List<PromotionDto> GetPromotionsWithOrderCount(int eventId, String status, String type);
    public PromotionDto GetPromotionById(int promotionId);
}
