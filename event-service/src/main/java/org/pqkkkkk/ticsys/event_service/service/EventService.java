package org.pqkkkkk.ticsys.event_service.service;


import java.time.LocalDate;
import java.util.Map;

import org.pqkkkkk.ticsys.event_service.dto.EventDto;
import org.pqkkkkk.ticsys.event_service.dto.request.EventRequest;
import org.pqkkkkk.ticsys.event_service.dto.response.EventResponse;
import org.pqkkkkk.ticsys.event_service.dto.response.GetEventsResponse;
import org.pqkkkkk.ticsys.event_service.dto.response.TimelyEventRevenueResponse;
import org.pqkkkkk.ticsys.event_service.dto.response.TimelyEventTicketCountResponse;
import org.springframework.web.multipart.MultipartFile;


public interface EventService {
    public EventResponse CreateEvent(EventRequest eventRequest, MultipartFile banner, MultipartFile seatMap);
    public EventDto GetEventById(int id, String includeStr);
    public GetEventsResponse GetEvents(String includeStr, Map<String, Object> filterMap);

    public TimelyEventRevenueResponse GetRevenue(String eventCategory,
                                                 LocalDate startDate, LocalDate endDate, String statasticsUnit);
    public TimelyEventTicketCountResponse GetTicketCount(String eventCategory,
                                                     LocalDate startDate, LocalDate endDate, String statasticsUnit);
    public TimelyEventRevenueResponse GetRevenueOfSpecificEvent(Integer eventId, Integer ticketTypeId,
                                                            LocalDate startDate, LocalDate endDate, String statasticsUnit);
    public TimelyEventTicketCountResponse GetTicketCountOfSpecificEvent(Integer eventId, Integer ticketTypeId,
                                                            LocalDate startDate, LocalDate endDate, String statasticsUnit);
}
