package com.hardcore.accounting.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.hardcore.accounting.dao.mapper.RecordTagMappingMapper;
import com.hardcore.accounting.model.persistence.RecordTagMapping;
import com.hardcore.accounting.model.persistence.Tag;

import com.google.common.collect.ImmutableList;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class RecordTagMappingDaoTest {
    @Mock
    private RecordTagMappingMapper recordTagMappingMapper;
    @InjectMocks
    private RecordTagMappingDaoImpl recordTagMappingDao;

    @Test
    void testInsertRecordTagMapping() {
        // Arrange
        val recordTagMapping = RecordTagMapping.builder()
                                               .id(1L)
                                               .recordId(1L)
                                               .tagId(1L)
                                               .status(1)
                                               .build();
        doReturn(1).when(recordTagMappingMapper).insertRecordTagMapping(any(RecordTagMapping.class));

        // Act
        recordTagMappingDao.insertRecordTagMapping(recordTagMapping);
        // Assert
        verify(recordTagMappingMapper).insertRecordTagMapping(any(RecordTagMapping.class));
    }

    @Test
    void testBatchInsertRecordTagMapping() {
        // Arrange
        val tagId = 1L;
        val userId = 1L;
        val recordId = 1L;
        val createTime = LocalDateTime.now();
        val tagList = ImmutableList.of(Tag.builder()
                                          .id(tagId)
                                          .description("playing")
                                          .userId(userId)
                                          .status(1)
                                          .createTime(createTime)
                                          .build());

        doReturn(1).when(recordTagMappingMapper).batchRecordTagMapping(anyList());

        // Act
        recordTagMappingDao.batchInsertRecordTagMapping(tagList, recordId);
        // Assert
        verify(recordTagMappingMapper).batchRecordTagMapping(anyList());
    }

    @Test
    void testGetRecordTagMappingListByRecordId() {
        // Arrange
        val id = 1L;
        val recordId = 1L;
        val tagId = 1L;
        val recordTagMappings = ImmutableList.of(RecordTagMapping.builder()
                                                                 .id(id)
                                                                 .tagId(tagId)
                                                                 .recordId(recordId)
                                                                 .status(1)
                                                                 .build());
        doReturn(recordTagMappings).when(recordTagMappingMapper).getRecordTagMappingListByRecordId(recordId);
        // Act
        val result = recordTagMappingDao.getRecordTagMappingListByRecordId(recordId);
        // Assert
        assertEquals(recordTagMappings, result);
        verify(recordTagMappingMapper).getRecordTagMappingListByRecordId(recordId);
    }

    @Test
    void testGetTagListByRecordId() {
        // Arrange
        val tagId = 1L;
        val userId = 1L;
        val recordId = 1L;
        val createTime = LocalDateTime.now();
        val tagList = ImmutableList.of(Tag.builder()
                                          .id(tagId)
                                          .description("playing")
                                          .userId(userId)
                                          .status(1)
                                          .createTime(createTime)
                                          .build());
        doReturn(tagList).when(recordTagMappingMapper).getTagListByRecordId(recordId);
        // Act
        val result = recordTagMappingDao.getTagListByRecordId(recordId);
        // Assert
        assertEquals(tagList, result);
        verify(recordTagMappingMapper).getTagListByRecordId(recordId);
    }

    @Test
    void testDeleteRecordTagMappingListByRecordId() {
        // Arrange
        val recordId = 1L;
        doReturn(1).when(recordTagMappingMapper).deleteRecordTagMappingList(eq(recordId));
        // Act
        recordTagMappingDao.deleteRecordTagMappingListByRecordId(recordId);
        // Assert
        verify(recordTagMappingMapper).deleteRecordTagMappingList(eq(recordId));
    }

}
