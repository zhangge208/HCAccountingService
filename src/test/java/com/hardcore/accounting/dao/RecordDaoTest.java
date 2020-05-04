package com.hardcore.accounting.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.hardcore.accounting.dao.mapper.RecordMapper;
import com.hardcore.accounting.model.persistence.Record;
import com.hardcore.accounting.model.persistence.Tag;

import com.google.common.collect.ImmutableList;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class RecordDaoTest {
    @Mock
    private RecordMapper recordMapper;

    @InjectMocks
    private RecordDaoImpl recordDao;

    @Test
    public void testGetRecordByRecordId() {
        // Arrange
        val recordId = 1000L;
        val userId = 100L;

        val record = Record.builder()
                           .id(recordId)
                           .userId(userId)
                           .status(1)
                           .build();
        doReturn(record).when(recordMapper).getRecordByRecordId(recordId);
        // Act
        val result = recordDao.getRecordByRecordId(recordId);
        // Assert
        assertEquals(record, result);
        verify(recordMapper).getRecordByRecordId(recordId);
    }

    @Test
    public void testInsertRecord() {
        // Arrange
        val tagId = 1000L;
        val userId = 100L;
        val createTime = LocalDateTime.now();

        val record = Record.builder()
                           .id(tagId)
                           .userId(userId)
                           .status(1)
                           .createTime(createTime)
                           .build();
        doReturn(1).when(recordMapper).insertRecord(record);
        // Act
        recordDao.insertRecord(record);
        // Assert
        verify(recordMapper).insertRecord(any(Record.class));
    }

    @Test
    void testUpdateRecord() {
        // Arrange
        val recordId = 10L;
        val firstTagId = 10L;
        val secondTagId = 10L;
        val userId = 100L;
        val amount = new BigDecimal("10.23");

        val tag1 = Tag.builder()
                      .id(firstTagId)
                      .userId(userId)
                      .description("playing")
                      .status(1)
                      .build();

        val tag2 = Tag.builder()
                      .id(secondTagId)
                      .userId(userId)
                      .description("eating")
                      .status(1)
                      .build();

        val tagList = ImmutableList.of(tag1, tag2);

        val record = Record.builder()
                           .id(recordId)
                           .status(1)
                           .tagList(tagList)
                           .amount(amount)
                           .note("Playing game.")
                           .category(0)
                           .build();

        doReturn(1).when(recordMapper).updateRecord(record);
        // Act
        recordDao.updateRecord(record);
        // Assert
        verify(recordMapper).updateRecord(any(Record.class));
    }
}
