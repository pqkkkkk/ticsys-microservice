package org.pqkkkkk.ticsys.promotion_service.infrastructure.client.mock;

import org.pqkkkkk.ticsys.promotion_service.domain.service.external.EventService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class EventMockClient implements EventService {

    @Override
    public boolean isValidEventDate(Long eventId, Long eventDateId) {
        return true;
    }

}
