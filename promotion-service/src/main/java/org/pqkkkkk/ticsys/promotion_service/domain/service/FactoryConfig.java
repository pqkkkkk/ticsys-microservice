package org.pqkkkkk.ticsys.promotion_service.domain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pqkkkkk.ticsys.promotion_service.domain.Constants.VoucherPromotionType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FactoryConfig {
    @Bean(name = "promotionCommandExecutors")
    public Map<VoucherPromotionType, PromotionCommandExecutor> promotionCommandExecutors(
        List<PromotionCommandExecutor> executors
    ){
        Map<VoucherPromotionType, PromotionCommandExecutor> map = new HashMap<>();

        for (PromotionCommandExecutor executor : executors) {
            VoucherPromotionType type = executor.getExecutorType();
            map.put(type, executor);
        }
        return map;
    }
    @Bean(name = "voucherQueryServices")
    public Map<VoucherPromotionType, VoucherService> voucherQueryServices(
        List<VoucherService> services
    ){
        Map<VoucherPromotionType, VoucherService> map = new HashMap<>();

        for (VoucherService service : services) {
            VoucherPromotionType type = service.getVoucherType();
            map.put(type, service);
        }
        return map;
    }
}
