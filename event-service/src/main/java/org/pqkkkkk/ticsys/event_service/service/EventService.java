package org.pqkkkkk.ticsys.event_service.service;

import org.pqkkkkk.ticsys.event_service.entity.Event;
import org.springframework.web.multipart.MultipartFile;

public interface EventService {
    public Event createEvent(Event event, Integer currentStep);
    public String uploadBanner(Long eventId, MultipartFile banner);
}
