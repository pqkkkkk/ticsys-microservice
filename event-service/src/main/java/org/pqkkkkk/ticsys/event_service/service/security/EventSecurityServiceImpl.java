package org.pqkkkkk.ticsys.event_service.service.security;

import org.pqkkkkk.ticsys.event_service.dao.event.query.IEventQueryDao;
import org.springframework.stereotype.Service;


@Service
public class EventSecurityServiceImpl implements EventSecurityService {
    private final IEventQueryDao eventQueryDao;

    public EventSecurityServiceImpl(IEventQueryDao eventQueryDao) {
        this.eventQueryDao = eventQueryDao;
    }
    @Override
    public boolean CheckEventOwner(Integer eventId) {
        try{
            String usernameOfEventOwner = eventQueryDao.GetUsernameOfEventOwner(eventId);

            if(usernameOfEventOwner == null){
                return false;
            }
            // String usernameOfCurrentUser = SecurityContextHolder.getContext().getAuthentication().getName();

            // return usernameOfCurrentUser.equals(usernameOfEventOwner);

            return true;
        }
        catch (Exception e){
            return false;
        }
    }

}
