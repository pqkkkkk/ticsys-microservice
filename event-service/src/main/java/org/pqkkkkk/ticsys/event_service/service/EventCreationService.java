package org.pqkkkkk.ticsys.event_service.service;

import org.pqkkkkk.ticsys.event_service.Contants.EventCreationStep;
import org.pqkkkkk.ticsys.event_service.entity.Event;

public interface EventCreationService {
    public boolean canHandle(Event event);
    public Event process(Event event, Integer currentStep);
    default public boolean isCompleted(Integer currentStep){
        return currentStep >= EventCreationStep.SETTINGS.getStep();
    }
    public EventCreationStep getStep();
}
