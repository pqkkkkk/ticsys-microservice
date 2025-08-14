package org.pqkkkkk.ticsys.event_service.api;

import org.pqkkkkk.ticsys.event_service.service.EventMemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/event/member")
public class EventMemberApi {
    private final EventMemberService eventMemberService;

    public EventMemberApi(EventMemberService eventMemberService) {
        this.eventMemberService = eventMemberService;
    }
}
