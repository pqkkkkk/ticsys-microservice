package org.pqkkkkk.ticsys.promotion_service.service.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PromotionSecurityServiceImpl implements PromotionSecurityService {
    //private final PublicEventService publicEventService;

    public PromotionSecurityServiceImpl() {
        //this.publicEventService = publicEventService;
    }
    private boolean IsEventOwnerOfPromotion(String userName,Integer eventId) {
        throw new UnsupportedOperationException("Method not implemented yet. Please implement the logic to check if the user is the owner of the event with ID: " + eventId);
    }
    @Override
    public boolean CanAccessCreateAndUpdatePromotion(Integer eventId) {
        try{
            List<String> currentRoles = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                        .stream()
                        .map(a -> a.getAuthority())
                        .collect(Collectors.toList());
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            if(currentRoles.contains("ROLE_ADMIN")){
                return true;
            }
            if(currentRoles.contains("ROLE_ORGANIZER")){
                return IsEventOwnerOfPromotion(currentUsername, eventId);
            }

            return false;
        }
        catch(Exception e){
            log.error("Error in CanAccessCreateAndUpdatePromotion", e);
            return false;
        }
    }
    @Override
    public boolean CanAccessGetPromotions(Integer eventId) {
        try{
            List<String> currentRoles = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                        .stream()
                        .map(a -> a.getAuthority())
                        .collect(Collectors.toList());
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

            if(currentRoles.contains("ROLE_ADMIN")){
                return true;
            }
            if(currentRoles.contains("ROLE_ORGANIZER")){
                return IsEventOwnerOfPromotion(currentUsername, eventId);
            }

            return false;
        }
        catch(Exception e){
            log.error("Error in CanAccessGetPromotions", e);
            return false;
        }
    }
    @Override
    public boolean CanGetPromotionsWithOrderCount(Integer eventId) {
        try{
            List<String> currentRoles = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                        .stream()
                        .map(a -> a.getAuthority())
                        .collect(Collectors.toList());
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

            if(currentRoles.contains("ROLE_ADMIN")){
                return true;
            }
            if(currentRoles.contains("ROLE_ORGANIZER")){
                return IsEventOwnerOfPromotion(currentUsername, eventId);
            }

            return false;
        }
        catch(Exception e){
            log.error("Error in CanGetPromotionsWithOrderCount", e);
            return false;
        }
    }
    @Override
    public Boolean CanAccessGetVoucherOfUser(String userId, Integer promotionId) {
        try{
            List<String> currentRoles = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                        .stream()
                        .map(a -> a.getAuthority())
                        .collect(Collectors.toList());
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

            if(currentRoles.contains("ROLE_ADMIN")){
                return true;
            }
            else if(currentRoles.contains("ROLE_USER")){
                if(userId == null){
                    return false;
                }
                return userId.equals(currentUsername);
            }

            return false;
        }
        catch(Exception e){
            log.error("Error in CanAccessGetVoucherOfUser", e);
            return false;
        }
    }

}
