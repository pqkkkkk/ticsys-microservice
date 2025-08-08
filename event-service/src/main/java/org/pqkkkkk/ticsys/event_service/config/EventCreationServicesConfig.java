package org.pqkkkkk.ticsys.event_service.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pqkkkkk.ticsys.event_service.Contants.EventCreationStep;
import org.pqkkkkk.ticsys.event_service.service.EventCreationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventCreationServicesConfig {
    @Bean
    public Map<EventCreationStep, EventCreationService> eventCreationServices(
        List<EventCreationService> services) {
        Map<EventCreationStep, EventCreationService> serviceMap = new HashMap<>();
        
        for (EventCreationService service : services) {
            EventCreationStep step = service.getStep();
            serviceMap.put(step, service);
        }
        
        return serviceMap;
    }
}
