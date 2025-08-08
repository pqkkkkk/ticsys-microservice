package org.pqkkkkk.ticsys.event_service.service.impl;

import java.util.Map;

import org.pqkkkkk.ticsys.event_service.Contants.EventCreationStep;
import org.pqkkkkk.ticsys.event_service.client.identity.IdentityClient;
import org.pqkkkkk.ticsys.event_service.client.storage.StorageClient;
import org.pqkkkkk.ticsys.event_service.dao.EventCommandDao;
import org.pqkkkkk.ticsys.event_service.dao.EventQueryDao;
import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.pqkkkkk.ticsys.event_service.exception.EventNotExistException;
import org.pqkkkkk.ticsys.event_service.exception.UserIsNotValidException;
import org.pqkkkkk.ticsys.event_service.service.EventCreationService;
import org.pqkkkkk.ticsys.event_service.service.EventService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;

@Service
public class EventServiceImpl implements EventService {

    private final EventCommandDao eventCommandDao;
    private final EventQueryDao eventQueryDao;
    private final StorageClient storageClient;
    private final IdentityClient identityClient;
    private Map<EventCreationStep, EventCreationService> eventCreationServices;

    public EventServiceImpl(EventCommandDao eventDao, EventQueryDao eventQueryDao,
                         StorageClient storageClient, IdentityClient identityClient,
                         Map<EventCreationStep, EventCreationService> eventCreationServices) {
        this.eventCommandDao = eventDao;
        this.eventQueryDao = eventQueryDao;
        this.storageClient = storageClient;
        this.identityClient = identityClient;
        this.eventCreationServices = eventCreationServices;
    }
    private EventCreationService getEventCreationService(Integer currentstep) {
        EventCreationService service = eventCreationServices.getOrDefault(EventCreationStep.fromStep(currentstep), null);
        if (service == null) {
            throw new IllegalArgumentException("Invalid step: " + currentstep);
        }

        return service;
    }
    @Override
    @Transactional
    public Event createEvent(Event event, Integer currentStep) {
        if(event == null){
            throw new EventNotExistException("Event cannot be null");
        }
        if(event.getEventId() == null) {
            throw new EventNotExistException("Event ID is required");
        }

        if(!identityClient.isValidUser(event.getUserId()))
            throw new UserIsNotValidException("User with ID is not valid");

        return getEventCreationService(currentStep).process(event, currentStep);
    }
    @Override
    public String uploadBanner(Long eventId, MultipartFile banner) {
        Event event = eventQueryDao.findEventById(eventId);

        if(event == null){
            throw new EventNotExistException(eventId);
        }

        String bannerUrl = storageClient.uploadFile(banner);

        event.setEventBanner(bannerUrl);
        eventCommandDao.updateEvent(event);

        return bannerUrl;
    }

}
