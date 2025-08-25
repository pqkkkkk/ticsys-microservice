package org.pqkkkkk.ticsys.event_service.service;

import org.pqkkkkk.ticsys.event_service.Contants.EventCreationStep;
import org.pqkkkkk.ticsys.event_service.entity.Event;

public interface EventUpdationService {
    public boolean canHandle(Event event);
    public Event process(Event event, Integer currentStep);
    public EventCreationStep getStep();
}
