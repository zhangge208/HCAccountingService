package com.hardcore.accounting.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hardcore.accounting.converter.c2s.TagC2SConverter;
import com.hardcore.accounting.exception.GlobalExceptionHandler;
import com.hardcore.accounting.manager.TagManager;
import com.hardcore.accounting.manager.UserInfoManager;
import com.hardcore.accounting.model.common.Tag;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class TagControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TagManager tagManager;

    @Mock
    private UserInfoManager userInfoManager;

    @Spy
    private TagC2SConverter tagC2SConverter;

    @InjectMocks
    private TagController tagController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(tagController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .build();
    }

    @AfterEach
    public void teardown() {
        reset(tagManager);
    }

    @Test
    void testCreateTag() throws Exception {
        // Arrange
        val tagId = 100L;
        val userId = 1900L;
        val description = "playing";

        val requestBody = com.hardcore.accounting.model.service.Tag.builder()
                                                                   .description(description)
                                                                   .userId(userId)
                                                                   .build();


        val tagInCommon = Tag.builder()
                             .id(tagId)
                             .description(description)
                             .userId(userId)
                             .status("ENABLE")
                             .build();

        val tag = com.hardcore.accounting.model.service.Tag.builder()
                                                           .id(tagId)
                                                           .description(description)
                                                           .userId(userId)
                                                           .status("ENABLE")
                                                           .build();

        doReturn(tagInCommon).when(tagManager).createTag(description, userId);

        // Act && Assert
        mockMvc.perform(post("/v1.0/tags")
                            .content(new ObjectMapper().writeValueAsString(requestBody))
                            .contentType("application/json"))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"))
               .andExpect(content().string(new ObjectMapper().writeValueAsString(tag)));

        verify(tagManager).createTag(anyString(), anyLong());

    }

    @Test
    void testGetTagByTagId() throws Exception {
        // Arrange
        val tagId = 100L;
        val userId = 1900L;
        val description = "playing";

        val tagInCommon = Tag.builder()
                             .id(tagId)
                             .description(description)
                             .userId(userId)
                             .status("ENABLE")
                             .build();


        val tag = com.hardcore.accounting.model.service.Tag.builder()
                                                           .id(tagId)
                                                           .description(description)
                                                           .userId(userId)
                                                           .status("ENABLE")
                                                           .build();


        doReturn(tagInCommon).when(tagManager).getTagByTagId(eq(tagId));

        // Act && Assert
        mockMvc.perform(get("/v1.0/tags/" + tagId)
                            .contentType("application/json"))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"))
               .andExpect(content().string(new ObjectMapper().writeValueAsString(tag)));

        verify(tagManager).getTagByTagId(anyLong());
    }

    @Test
    void testUpdateTag() throws Exception {

        // Arrange
        val tagId = 100L;
        val userId = 1900L;
        val description = "playing";

        val userInfo = com.hardcore.accounting.model.common.UserInfo.builder()
                                                                    .username("hardcore")
                                                                    .password("hardcore")
                                                                    .id(userId)
                                                                    .build();

        val requestBody = com.hardcore.accounting.model.service.Tag.builder()
                                                                   .description(description)
                                                                   .userId(userId)
                                                                   .status("DISABLE")
                                                                   .build();

        val tagInCommon = Tag.builder()
                             .id(tagId)
                             .description(description)
                             .userId(userId)
                             .status("DISABLE")
                             .build();


        val tag = com.hardcore.accounting.model.service.Tag.builder()
                                                           .id(tagId)
                                                           .description(description)
                                                           .userId(userId)
                                                           .status("DISABLE")
                                                           .build();

        doReturn(tagInCommon).when(tagManager).updateTag(any(Tag.class));
        doReturn(userInfo).when(userInfoManager).getUserInfoByUserId(userId);

        // Act && Assert
        mockMvc.perform(put("/v1.0/tags/" + tagId)
                            .content(new ObjectMapper().writeValueAsString(requestBody))
                            .contentType("application/json"))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"))
               .andExpect(content().string(new ObjectMapper().writeValueAsString(tag)));

        verify(tagManager).updateTag(any(Tag.class));
        verify(userInfoManager).getUserInfoByUserId(anyLong());
    }

}
