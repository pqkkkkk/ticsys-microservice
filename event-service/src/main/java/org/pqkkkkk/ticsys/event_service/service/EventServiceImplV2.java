package org.pqkkkkk.ticsys.event_service.service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pqkkkkk.ticsys.event_service.dao.event.IEventDao;
import org.pqkkkkk.ticsys.event_service.dao.ticket.ITicketDao;
import org.pqkkkkk.ticsys.event_service.dto.EventDto;
import org.pqkkkkk.ticsys.event_service.dto.TimelyEventDataDto;
import org.pqkkkkk.ticsys.event_service.dto.request.EventRequest;
import org.pqkkkkk.ticsys.event_service.dto.response.EventResponse;
import org.pqkkkkk.ticsys.event_service.dto.response.GetEventsResponse;
import org.pqkkkkk.ticsys.event_service.dto.response.TimelyEventRevenueResponse;
import org.pqkkkkk.ticsys.event_service.dto.response.TimelyEventTicketCountResponse;
import org.pqkkkkk.ticsys.event_service.model.Event;
import org.pqkkkkk.ticsys.event_service.model.Ticket;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Primary
@Service
@Slf4j
public class EventServiceImplV2 implements EventService {
    private final IEventDao eventDao;
    private final ITicketDao ticketDao;
    //private final CloudinaryService cloudinaryService;
    //private final RedisService redisService;
    //private final PublicOrderService publicOrderService;

    public EventServiceImplV2(IEventDao eventDao, ITicketDao ticketDao) {
        this.eventDao = eventDao;
        this.ticketDao = ticketDao;
        //this.cloudinaryService = cloudinaryService;
        //this.redisService = redisService;
        //this.publicOrderService = publicOrderService;
    }
    @Override
    @Transactional
    public EventResponse CreateEvent(EventRequest eventRequest, MultipartFile banner, MultipartFile seatMap) {
        String bannerPath = "";
        String seatMapPath = "";
        try{
           
            eventRequest.getEvent().setBannerPath(bannerPath.equals("empty") ? null : bannerPath);
            eventRequest.getEvent().setSeatMapPath(seatMapPath.equals("empty") ? null : seatMapPath);
            int eventId = eventDao.CreateEvent(eventRequest.getEvent());
            if(eventId == -1){
                throw new Exception("Failed to create event");
            }

            for(Ticket ticket : eventRequest.getTickets()){
                ticket.setEventId(eventId);
                if(!ticketDao.AddTicket(ticket)){
                    throw new Exception("Failed to create ticket");
                }
            }

            return EventResponse.builder().message("success").build();
        }
        catch (Exception e){
            log.error("Failed to create event: " + e.getMessage());

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return EventResponse.builder().message(e.getMessage()).build();
        }
    }
    @Override
    public EventDto GetEventById(int id, String includeStr) {
        try{
            Event event = eventDao.GetEventById(id);
            EventDto eventDto = EventDto.builder().event(event).build();

            List<Ticket> tickets = ticketDao.GetTicketsOfEvent(event.getId());

            int minPriceOfTicket = tickets.stream().mapToInt(Ticket::getPrice)
                                    .min()
                                    .orElse(0);

            eventDto.setMinPriceOfTicket(minPriceOfTicket);

            if(includeStr != null && includeStr.contains("tickets")){
                eventDto.setTickets(tickets);
            }

            return eventDto;
        }
        catch(Exception e)
        {
            return null;
        }
    }
    @Override
    public GetEventsResponse GetEvents(String includeStr, Map<String, Object> filterMap) {
        try{
            List<EventDto> eventDtos = new ArrayList<>();
            List<Event> events = eventDao.GetEvents(filterMap);

            for(Event event : events)
            {
                EventDto eventDto = new EventDto();
                eventDto.setEvent(event);

                List<Ticket> tickets = ticketDao.GetTicketsOfEvent(event.getId());

                int minPriceOfTicket = tickets.stream().mapToInt(Ticket::getPrice)
                                    .min()
                                    .orElse(0);
                eventDto.setMinPriceOfTicket(minPriceOfTicket);

                if(includeStr != null && includeStr.contains("tickets")){
                    eventDto.setTickets(tickets);
                }

                eventDtos.add(eventDto);
            }

            return GetEventsResponse.builder().message("success").eventDtos(eventDtos).build();
        }
        catch (Exception e)
        {
            return GetEventsResponse.builder().message(e.getMessage()).eventDtos(null).build();
        }
    }
    private String GetLabel(String statasticsUnit, LocalDate date){
       String label = "";

       switch (statasticsUnit) {
        case "date":
            label = date.toString();
            break;
        case "month":
            label = date.getYear() + "-" + String.format("%02d", date.getMonthValue());
            break;
        case "year":
            label = date.getYear() + "";
            break;
        default:
            break;
       }

        return label;
    }
    
    private Map<String, Object> GenerateRevenueMap(LocalDate startDate, LocalDate endDate, String statasticsUnit){
        Map<String, Object> revenueMap = new HashMap<>();
        LocalDate currentDate = startDate;

        switch (statasticsUnit) {
            case "date":
                while(currentDate.isBefore(endDate)){
                    revenueMap.put(currentDate.toString(), 0);
                    currentDate = currentDate.plusDays(1);
                }
                break;
            case "month":
                while(currentDate.isBefore(endDate)){
                    revenueMap.put(currentDate.getYear() + "-" + String.format("%02d", currentDate.getMonthValue()), 0);
                    currentDate = currentDate.withDayOfMonth(1).plusMonths(1);
                }
                break;
            case "year":
                while(currentDate.isBefore(endDate)){
                    revenueMap.put(currentDate.getYear() + "", 0);
                    currentDate = currentDate.withDayOfYear(1).plusYears(1);
                }
                break;
            default:
                break;
        }
        return revenueMap;
    }
    @Override
    public TimelyEventRevenueResponse GetRevenue(String eventCategory,
                                        LocalDate startDate, LocalDate endDate, String statasticsUnit) {
        try{
            if(startDate.isAfter(endDate)){
               throw new Exception("Invalid date range");
            }
            if(!statasticsUnit.equals("date") && !statasticsUnit.equals("month") && !statasticsUnit.equals("year")){
               throw new Exception("Invalid statastics unit");
            }

            TimelyEventRevenueResponse result = TimelyEventRevenueResponse.builder()
                                                .totalRevenue(null)
                                                .revenues(null)
                                                .message("error").build();
                                        

            Integer totalRevenue = 0;
            Map<String,Object> revenueMap = GenerateRevenueMap(startDate, endDate, statasticsUnit);

   
            List<TimelyEventDataDto> revenues = new ArrayList<>();
            for(Map.Entry<String, Object> entry : revenueMap.entrySet()){
                revenues.add(TimelyEventDataDto.builder()
                                .label(entry.getKey())
                                .value((Integer)entry.getValue())
                                .build());
            }

            result.setRevenues(revenues);
            result.setMessage("success");
            result.setTotalRevenue(totalRevenue);

            return result;
        }
        catch(Exception e){
            log.error("Error: " + e.getMessage());

            return TimelyEventRevenueResponse.builder()
                        .totalRevenue(null)
                        .revenues(null)        
                        .message("error").build();
        }
    }
    @Override
    public TimelyEventTicketCountResponse GetTicketCount(String eventCategory, LocalDate startDate, LocalDate endDate,
            String statasticsUnit) {
                try{
                    if(startDate.isAfter(endDate)){
                       throw new Exception("Invalid date range");
                    }
                    if(!statasticsUnit.equals("date") && !statasticsUnit.equals("month") && !statasticsUnit.equals("year")){
                       throw new Exception("Invalid statastics unit");
                    }
        
                    TimelyEventTicketCountResponse result = TimelyEventTicketCountResponse.builder()
                                                        .totalTicketCount(0)
                                                        .ticketCounts(null)
                                                        .message("error").build();
                                                
        
                    Integer totalTicketCount = 0;
                    Map<String,Object> ticketCountMap = GenerateRevenueMap(startDate, endDate, statasticsUnit);
        
        
                    List<TimelyEventDataDto> ticketCounts = new ArrayList<>();
                    for(Map.Entry<String, Object> entry : ticketCountMap.entrySet()){
                        ticketCounts.add(TimelyEventDataDto.builder()
                                        .label(entry.getKey())
                                        .value((Integer)entry.getValue())
                                        .build());
                    }
        
                    result.setTicketCounts(ticketCounts);
                    result.setMessage("success");
                    result.setTotalTicketCount(totalTicketCount);
        
                    return result;
                }
                catch(Exception e){
                    log.error("Error: " + e.getMessage());
        
                    return TimelyEventTicketCountResponse.builder()
                                .totalTicketCount(null)
                                .ticketCounts(null)        
                                .message("error").build();
                }
    }
    @Override
    public TimelyEventRevenueResponse GetRevenueOfSpecificEvent(Integer eventId, Integer ticketTypeId,
            LocalDate startDate, LocalDate endDate, String statasticsUnit) {
        try{
            if(startDate.isAfter(endDate)){
                throw new Exception("Invalid date range");
            }
            if(!statasticsUnit.equals("date") && !statasticsUnit.equals("month") && !statasticsUnit.equals("year")){
                throw new Exception("Invalid statastics unit");
            }

            TimelyEventRevenueResponse result = TimelyEventRevenueResponse.builder()
                                                .totalRevenue(null)
                                                .revenues(null)
                                                .message("error").build();
                            

            Map<String,Object> revenueMap = GenerateRevenueMap(startDate, endDate, statasticsUnit);
            Integer totalRevenue = 0;

            List<TimelyEventDataDto> revenues = new ArrayList<>();
            for(Map.Entry<String, Object> entry : revenueMap.entrySet()){
                revenues.add(TimelyEventDataDto.builder()
                                .label(entry.getKey())
                                .value((Integer)entry.getValue())
                                .build());
            }

            result.setRevenues(revenues);
            result.setMessage("success");
            result.setTotalRevenue(totalRevenue);

            return result;
        }
        catch(Exception e){
            log.error("Error: " + e.getMessage());

            return TimelyEventRevenueResponse.builder()
                        .totalRevenue(null)
                        .revenues(null)        
                        .message("error").build();
        }
    }
    @Override
    public TimelyEventTicketCountResponse GetTicketCountOfSpecificEvent(Integer eventId, Integer ticketTypeId,
                                                            LocalDate startDate, LocalDate endDate, String statasticsUnit) {
                try{
                    if(startDate.isAfter(endDate)){
                       throw new Exception("Invalid date range");
                    }
                    if(!statasticsUnit.equals("date") && !statasticsUnit.equals("month") && !statasticsUnit.equals("year")){
                       throw new Exception("Invalid statastics unit");
                    }
        
                    TimelyEventTicketCountResponse result = TimelyEventTicketCountResponse.builder()
                                                        .totalTicketCount(0)
                                                        .ticketCounts(null)
                                                        .message("error").build();
                                                
        
                    Integer totalTicketCount = 0;
                    Map<String,Object> ticketCountMap = GenerateRevenueMap(startDate, endDate, statasticsUnit);
                
                    List<TimelyEventDataDto> ticketCounts = new ArrayList<>();
                    for(Map.Entry<String, Object> entry : ticketCountMap.entrySet()){
                        ticketCounts.add(TimelyEventDataDto.builder()
                                        .label(entry.getKey())
                                        .value((Integer)entry.getValue())
                                        .build());
                    }
        
                    result.setTicketCounts(ticketCounts);
                    result.setMessage("success");
                    result.setTotalTicketCount(totalTicketCount);
        
                    return result;
                }
                catch(Exception e){
                    log.error("Error: " + e.getMessage());
        
                    return TimelyEventTicketCountResponse.builder()
                                .totalTicketCount(null)
                                .ticketCounts(null)        
                                .message("error").build();
                }
    }
}
