package org.pqkkkkk.ticsys.event_service.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/event/test")
public class EventTestController {
    @GetMapping
    public String HelloFromEventService() {
        return "Hello from Event Service!";
    }
    
}
