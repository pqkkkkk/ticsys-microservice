package org.pqkkkkk.ticsys.event_service.service.impl;

import org.pqkkkkk.ticsys.event_service.Contants.EventCreationStep;
import org.pqkkkkk.ticsys.event_service.dao.EventCommandDao;
import org.pqkkkkk.ticsys.event_service.dao.EventQueryDao;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.pqkkkkk.ticsys.event_service.service.EventUpdationService;
import org.springframework.stereotype.Service;

@Service
public class SettingUpdationService extends SettingCreationService implements EventUpdationService {
    private final EventCommandDao eventCommandDao;
    private final EventQueryDao eventQueryDao;

    public SettingUpdationService(EventCommandDao eventCommandDao, EventQueryDao eventQueryDao) {
        super(eventCommandDao, eventQueryDao);
        this.eventCommandDao = eventCommandDao;
        this.eventQueryDao = eventQueryDao;
    }

    @Override
    public boolean canHandle(Event event) {
        return super.canHandle(event);
    }

    @Override
    public Event process(Event event, Integer currentStep) {
        return super.process(event, currentStep);
    }
    @Override
    public EventCreationStep getStep() {
        return EventCreationStep.SETTINGS;
    }

}
