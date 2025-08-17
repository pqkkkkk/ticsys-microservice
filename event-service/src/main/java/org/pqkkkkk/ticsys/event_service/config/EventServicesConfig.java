package org.pqkkkkk.ticsys.event_service.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pqkkkkk.ticsys.event_service.Contants.EventCreationStep;
import org.pqkkkkk.ticsys.event_service.service.EventCreationService;
import org.pqkkkkk.ticsys.event_service.service.EventUpdationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventServicesConfig {
    @Bean(name = "eventCreationServices")
    public Map<EventCreationStep, EventCreationService> eventCreationServices(
        List<EventCreationService> services) {
        Map<EventCreationStep, EventCreationService> serviceMap = new HashMap<>();
        
        for (EventCreationService service : services) {
            if (!(service instanceof EventUpdationService)) {
                EventCreationStep step = service.getStep();
                serviceMap.put(step, service);
            }
        }
        
        return serviceMap;
    }

    @Bean(name = "eventUpdationServices")
    public Map<EventCreationStep, EventUpdationService> eventUpdationServices(
        List<EventUpdationService> services) {
        Map<EventCreationStep, EventUpdationService> serviceMap = new HashMap<>();
        
        for (EventUpdationService service : services) {
            EventCreationStep step = service.getStep();
            serviceMap.put(step, service);
        }
        
        return serviceMap;
    }
}
