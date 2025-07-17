package org.pqkkkkk.ticsys.promotion_service.dao.promotion.command;

import org.pqkkkkk.ticsys.promotion_service.model.Promotion;

public interface IPromotionCommandDao {
    public int CreatePromotion(Promotion promotion);
    public int UpdatePromotion(Promotion promotion);
}
