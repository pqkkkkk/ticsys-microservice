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
import org.pqkkkkk.ticsys.event_service.service.EventUpdationService;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private Map<EventCreationStep, EventUpdationService> eventUpdationServices;

    public EventServiceImpl(EventCommandDao eventDao, EventQueryDao eventQueryDao,
                         StorageClient storageClient, IdentityClient identityClient,
                        @Qualifier("eventCreationServices") Map<EventCreationStep, EventCreationService> eventCreationServices,
                        @Qualifier("eventUpdationServices") Map<EventCreationStep, EventUpdationService> eventUpdationServices) {
        this.eventCommandDao = eventDao;
        this.eventQueryDao = eventQueryDao;
        this.storageClient = storageClient;
        this.identityClient = identityClient;
        this.eventCreationServices = eventCreationServices;
        this.eventUpdationServices = eventUpdationServices;
    }
    private EventCreationService getEventCreationService(Integer currentstep) {
        EventCreationService service = eventCreationServices.getOrDefault(EventCreationStep.fromStep(currentstep), null);
        if (service == null) {
            throw new IllegalArgumentException("Invalid step: " + currentstep);
        }

        return service;
    }

    private EventUpdationService getEventUpdationService(Integer currentstep) {
        EventUpdationService service = eventUpdationServices.getOrDefault(EventCreationStep.fromStep(currentstep), null);
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

        if(!identityClient.isValidUser(event.getUserId()))
            throw new UserIsNotValidException("User with ID is not valid");

        Event newEvent = getEventCreationService(currentStep).process(event, currentStep);

        return newEvent;
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

    @Override
    @Transactional
    public Event updateEvent(Event event, Integer currentStep) {
        if(event == null){
            throw new EventNotExistException("Event cannot be null");
        }

        if(!identityClient.isValidUser(event.getUserId()))
            throw new UserIsNotValidException("User with ID is not valid");

        return getEventUpdationService(currentStep).process(event, currentStep);
    }

}
