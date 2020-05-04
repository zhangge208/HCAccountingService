package com.hardcore.accounting.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hardcore.accounting.exception.GlobalExceptionHandler;
import com.hardcore.accounting.manager.UserInfoManager;
import com.hardcore.accounting.model.service.UserInfo;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

    @Mock
    private UserInfoManager userInfoManager;

    @InjectMocks
    private SessionController sessionController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(sessionController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .build();
    }

    @Test
    void testLogin() throws Exception {
        // Arrange
        val username = "hardcore";
        val password = "hardcore";

        val userInfo = UserInfo.builder()
                               .username(username)
                               .password(password)
                               .build();

        doNothing().when(userInfoManager).login(username, password);

        // Act && Assert
        mockMvc.perform(post("/v1.0/session")
                            .contentType("application/json")
                            .content(new ObjectMapper().writeValueAsString(userInfo))
                            .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"))
               .andExpect(content().string("{\"status\":\"success\",\"username\":\"hardcore\"}"));

        verify(userInfoManager).login(username, password);

    }
}
