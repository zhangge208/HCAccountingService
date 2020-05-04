package com.hardcore.accounting.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.hardcore.accounting.converter.c2s.RecordC2SConverter;
import com.hardcore.accounting.exception.GlobalExceptionHandler;
import com.hardcore.accounting.manager.RecordManager;
import com.hardcore.accounting.manager.UserInfoManager;
import com.hardcore.accounting.model.common.Record;
import com.hardcore.accounting.model.common.Tag;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class RecordControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RecordManager recordManager;

    @Mock
    private UserInfoManager userInfoManager;

    @Spy
    private RecordC2SConverter recordC2SConverter;

    @InjectMocks
    private RecordController recordController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(recordController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .build();
    }

    @Test
    void testCreateRecord() throws Exception {
        // Arrange
        val tagId = 100L;
        val userId = 1900L;
        val amount = new BigDecimal("100.25");

        val tag = com.hardcore.accounting.model.service.Tag.builder()
                                                           .id(tagId)
                                                           .build();
        val requestBody = com.hardcore.accounting.model.service.Record.builder()
                                                                      .amount(amount)
                                                                      .userId(userId)
                                                                      .note("xxxx")
                                                                      .category("INCOME")
                                                                      .tagList(ImmutableList.of(tag))
                                                                      .build();

        val recordInCommon = Record.builder()
                                   .amount(amount)
                                   .userId(userId)
                                   .note("xxxx")
                                   .category("INCOME")
                                   .tagList(ImmutableList.of(com.hardcore.accounting.model.common.Tag.builder()
                                                                                                     .id(tagId)
                                                                                                     .build()))
                                   .status("ENABLE")
                                   .build();

        val record = com.hardcore.accounting.model.service.Record.builder()
                                                                 .amount(amount)
                                                                 .userId(userId)
                                                                 .note("xxxx")
                                                                 .category("INCOME")
                                                                 .tagList(ImmutableList.of(tag))
                                                                 .build();

        doReturn(recordInCommon).when(recordManager).createRecord(any(Record.class));

        // Act && Assert
        mockMvc.perform(post("/v1.0/records")
                            .content(new ObjectMapper().writeValueAsString(requestBody))
                            .contentType("application/json"))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"))
               .andExpect(content().string(new ObjectMapper().writeValueAsString(record)));

        verify(recordManager).createRecord(any(Record.class));
    }

    @Test
    void testGetRecordByRecordId() throws Exception {

        // Arrange
        val recordId = 100L;
        val tagId = 1L;
        val userId = 1900L;
        val amount = new BigDecimal("100.25");
        val tagInCommon = Tag.builder()
                             .id(tagId)
                             .build();
        val tag = com.hardcore.accounting.model.service.Tag.builder()
                                                           .id(tagId)
                                                           .build();
        val recordInCommon = Record.builder()
                                   .amount(amount)
                                   .userId(userId)
                                   .note("xxxx")
                                   .category("INCOME")
                                   .tagList(ImmutableList.of(tagInCommon))
                                   .build();
        val record = com.hardcore.accounting.model.service.Record.builder()
                                                                 .amount(amount)
                                                                 .userId(userId)
                                                                 .note("xxxx")
                                                                 .category("INCOME")
                                                                 .tagList(ImmutableList.of(tag))
                                                                 .build();

        doReturn(recordInCommon).when(recordManager).getRecordByRecordId(eq(recordId));

        // Act && Assert
        mockMvc.perform(get("/v1.0/records/" + recordId)
                            .contentType("application/json"))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"))
               .andExpect(content().string(new ObjectMapper().writeValueAsString(record)));

        verify(recordManager).getRecordByRecordId(anyLong());
    }

    @Test
    void testUpdateRecord() throws Exception {

        // Arrange
        val recordId = 100L;
        val tagId = 10L;
        val userId = 1900L;
        val description = "playing";
        val amount = new BigDecimal("100.25");
        val tagInCommon = Tag.builder()
                             .id(tagId)
                             .build();
        val tag = com.hardcore.accounting.model.service.Tag.builder()
                                                           .id(tagId)
                                                           .build();
        val userInfo = com.hardcore.accounting.model.common.UserInfo.builder()
                                                                    .username("hardcore")
                                                                    .password("hardcore")
                                                                    .id(userId)
                                                                    .build();

        val requestBody = com.hardcore.accounting.model.service.Record.builder()
                                                                      .amount(amount)
                                                                      .userId(userId)
                                                                      .note("xxxx")
                                                                      .category("INCOME")
                                                                      .tagList(ImmutableList.of(tag))
                                                                      .build();

        val recordInCommon = Record.builder()
                                   .amount(amount)
                                   .userId(userId)
                                   .note("xxxx")
                                   .category("INCOME")
                                   .tagList(ImmutableList.of(tagInCommon))
                                   .build();
        val record = com.hardcore.accounting.model.service.Record.builder()
                                                                 .amount(amount)
                                                                 .userId(userId)
                                                                 .note("xxxx")
                                                                 .category("INCOME")
                                                                 .tagList(ImmutableList.of(tag))
                                                                 .build();

        doReturn(recordInCommon).when(recordManager).updateRecord(any(Record.class));
        doReturn(userInfo).when(userInfoManager).getUserInfoByUserId(userId);

        // Act && Assert
        mockMvc.perform(put("/v1.0/records/" + recordId)
                            .content(new ObjectMapper().writeValueAsString(requestBody))
                            .contentType("application/json"))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"))
               .andExpect(content().string(new ObjectMapper().writeValueAsString(record)));

        verify(recordManager).updateRecord(any(Record.class));
        verify(userInfoManager).getUserInfoByUserId(anyLong());
    }
}
