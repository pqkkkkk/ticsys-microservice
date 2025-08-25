package org.pqkkkkk.ticsys.event_service.api;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.pqkkkkk.ticsys.event_service.Contants.UserRoleInEvent;
import org.pqkkkkk.ticsys.event_service.dto.request.EventMemberRequest.CreateEventMemberRequest;
import org.pqkkkkk.ticsys.event_service.service.EventMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(value =  EventMemberApi.class,
excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class EventMemberApiTest {
    private static final String BASE_URL = "/api/v1/event/member";
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private EventMemberService eventMemberService;

    // ------------------- Test cases for addMember api
    @Test
    public void addMember_WithNoUserId_ShowBadRequest() throws Exception {
        CreateEventMemberRequest request = new CreateEventMemberRequest(null, 1L,
                                UserRoleInEvent.MEMBER);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    public void addMember_WithNoEventId_ShowBadRequest() throws Exception {
        CreateEventMemberRequest request = new CreateEventMemberRequest(1L, null,
                                UserRoleInEvent.MEMBER);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    public void addMember_WithNoUserRole_ShowBadRequest() throws Exception {
        CreateEventMemberRequest request = new CreateEventMemberRequest(1L, 1L,
                                null);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    public void addMember_WithValidRequest_ShowCreated() throws Exception {
        CreateEventMemberRequest request = new CreateEventMemberRequest(1L, 1L,
                                UserRoleInEvent.MEMBER);

        when(eventMemberService.addEventMember(request.toEntity())).thenReturn(request.toEntity());

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    // ------------------------- Test cases for removeMember api
}
