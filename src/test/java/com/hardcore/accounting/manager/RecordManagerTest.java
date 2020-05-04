package com.hardcore.accounting.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hardcore.accounting.converter.p2c.RecordP2CConverter;
import com.hardcore.accounting.dao.RecordDao;
import com.hardcore.accounting.dao.RecordTagMappingDao;
import com.hardcore.accounting.dao.TagDao;
import com.hardcore.accounting.exception.ResourceNotFoundException;
import com.hardcore.accounting.model.persistence.Record;
import com.hardcore.accounting.model.persistence.Tag;

import com.google.common.collect.ImmutableList;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class RecordManagerTest {

    @Mock
    private TagDao tagDao;

    @Mock
    private RecordDao recordDao;

    @Mock
    private RecordTagMappingDao recordTagMappingDao;

    @Spy
    private RecordP2CConverter recordP2CConverter;

    @InjectMocks
    private RecordManagerImpl recordManager;

    @Test
    void testCreateRecord() {
        // Arrange
        val recordId = 1L;
        val tagId = 10L;
        val userId = 100L;
        val amount = new BigDecimal("10.23");
        val tag = Tag.builder()
                     .id(tagId)
                     .userId(userId)
                     .description("playing")
                     .status(1)
                     .build();

        val tagList = ImmutableList.of(tag);
        val record = com.hardcore.accounting.model.common.Record.builder()
                                                                .id(recordId)
                                                                .amount(amount)
                                                                .userId(userId)
                                                                .note("Playing game")
                                                                .category("INCOME")
                                                                .tagList(ImmutableList.of(com.hardcore.accounting.model.common.Tag.builder()
                                                                                                                                  .id(tagId)
                                                                                                                                  .userId(userId)
                                                                                                                                  .status("ENABLE")
                                                                                                                                  .description("playing")
                                                                                                                                  .build()))
                                                                .status("ENABLE")
                                                                .build();

        val recordInDB = Record.builder()
                               .id(recordId)
                               .status(1)
                               .userId(userId)
                               .tagList(tagList)
                               .amount(amount)
                               .note("Playing game")
                               .category(0)
                               .build();


        doNothing().when(recordDao).insertRecord(any(Record.class));
        doReturn(tagList).when(tagDao).getTagListByIds(anyList());
        doNothing().when(recordTagMappingDao).batchInsertRecordTagMapping(tagList, recordId);
        doReturn(recordInDB).when(recordDao).getRecordByRecordId(recordId);

        // Act
        val result = recordManager.createRecord(record);
        // Assert
        assertThat(result).isNotNull()
                          .hasFieldOrPropertyWithValue("id", recordId)
                          .hasFieldOrPropertyWithValue("userId", userId)
                          .hasFieldOrPropertyWithValue("note", "Playing game")
                          .hasFieldOrPropertyWithValue("category", "OUTCOME")
                          .hasFieldOrPropertyWithValue("tagList", ImmutableList.of(com.hardcore.accounting.model.common.Tag.builder()
                                                                                                                           .id(tagId)
                                                                                                                           .status("ENABLE")
                                                                                                                           .userId(userId)
                                                                                                                           .description("playing")
                                                                                                                           .build()))
                          .hasFieldOrPropertyWithValue("amount", amount);
        verify(recordDao).insertRecord(any(Record.class));
        verify(tagDao).getTagListByIds(anyList());
        verify(recordTagMappingDao).batchInsertRecordTagMapping(eq(tagList), anyLong());
        verify(recordDao).getRecordByRecordId(recordId);
    }

    @Test
    void testUpdateTag() {
        // Arrange
        val recordId = 100L;
        val tagId = 10L;
        val newTagId = 19L;
        val userId = 1L;
        val amount = new BigDecimal("13.24");
        val tag = Tag.builder()
                     .id(tagId)
                     .userId(userId)
                     .description("playing")
                     .status(1)
                     .build();

        val updatedTag = Tag.builder()
                            .id(newTagId)
                            .userId(userId)
                            .description("eating")
                            .status(1)
                            .build();
        val tagList = ImmutableList.of(tag);
        val updateTagList = ImmutableList.of(updatedTag);
        val recordInDB = Record.builder()
                               .id(recordId)
                               .userId(userId)
                               .status(1)
                               .tagList(tagList)
                               .amount(amount)
                               .note("Happy")
                               .category(0)
                               .build();
        val updatedRecordInDB = Record.builder()
                                      .id(recordId)
                                      .userId(userId)
                                      .status(1)
                                      .tagList(updateTagList)
                                      .amount(amount)
                                      .note("Happy")
                                      .category(0)
                                      .build();

        val record = com.hardcore.accounting.model.common.Record.builder()
                                                                .id(recordId)
                                                                .userId(userId)
                                                                .note("Happy")
                                                                .amount(amount)
                                                                .category("INCOME")
                                                                .tagList(ImmutableList.of(com.hardcore.accounting.model.common.Tag.builder()
                                                                                                                                  .id(newTagId)
                                                                                                                                  .status("ENABLE")
                                                                                                                                  .userId(userId)
                                                                                                                                  .description("eating")
                                                                                                                                  .build()))
                                                                .build();

        when(recordDao.getRecordByRecordId(recordId)).thenReturn(recordInDB)
                                                     .thenReturn(updatedRecordInDB);

        doReturn(updateTagList).when(tagDao).getTagListByIds(anyList());
        doNothing().when(recordTagMappingDao).deleteRecordTagMappingListByRecordId(recordId);
        doNothing().when(recordTagMappingDao).batchInsertRecordTagMapping(updateTagList, recordId);
        // Act
        val result = recordManager.updateRecord(record);
        // Assert
        assertThat(result).isNotNull()
                          .hasFieldOrPropertyWithValue("id", recordId)
                          .hasFieldOrPropertyWithValue("userId", userId)
                          .hasFieldOrPropertyWithValue("note", "Happy")
                          .hasFieldOrPropertyWithValue("category", "OUTCOME")
                          .hasFieldOrPropertyWithValue("tagList", ImmutableList.of(com.hardcore.accounting.model.common.Tag.builder()
                                                                                                                           .id(newTagId)
                                                                                                                           .status("ENABLE")
                                                                                                                           .userId(userId)
                                                                                                                           .description("eating")
                                                                                                                           .build()))
                          .hasFieldOrPropertyWithValue("amount", amount);
        verify(recordDao, times(2)).getRecordByRecordId(recordId);
        verify(tagDao).getTagListByIds(anyList());
        verify(recordTagMappingDao).deleteRecordTagMappingListByRecordId(recordId);
        verify(recordTagMappingDao).batchInsertRecordTagMapping(updateTagList, recordId);
    }

    @Test
    void testGetTagByTagId() {
        // Arrange
        val recordId = 1L;
        val tagId = 10L;
        val userId = 100L;
        val amount = new BigDecimal("10.23");
        val tag = Tag.builder()
                     .id(tagId)
                     .userId(userId)
                     .description("playing")
                     .status(1)
                     .build();
        val tagList = ImmutableList.of(tag);
        val recordInDB = Record.builder()
                               .id(recordId)
                               .userId(userId)
                               .status(1)
                               .tagList(tagList)
                               .amount(amount)
                               .note("Playing game")
                               .category(0)
                               .build();

        doReturn(recordInDB).when(recordDao).getRecordByRecordId(recordId);
        // Act
        val result = recordManager.getRecordByRecordId(recordId);
        // Assert
        assertThat(result).isNotNull()
                          .hasFieldOrPropertyWithValue("id", recordId)
                          .hasFieldOrPropertyWithValue("userId", userId)
                          .hasFieldOrPropertyWithValue("note", "Playing game")
                          .hasFieldOrPropertyWithValue("category", "OUTCOME")
                          .hasFieldOrPropertyWithValue("tagList", ImmutableList.of(com.hardcore.accounting.model.common.Tag.builder()
                                                                                                                                 .id(tagId)
                                                                                                                                 .status("ENABLE")
                                                                                                                                 .userId(userId)
                                                                                                                                 .description("playing")
                                                                                                                                 .build()))
                          .hasFieldOrPropertyWithValue("amount", amount);

        verify(recordDao).getRecordByRecordId(recordId);
    }

    @Test
    void testGetTagByInvalidTagId() {
        // Arrange
        val recordId = 1L;
        doReturn(null).when(recordDao).getRecordByRecordId(recordId);

        // Act && Assert
        assertThrows(ResourceNotFoundException.class, () -> recordManager.getRecordByRecordId(recordId));
        verify(recordDao).getRecordByRecordId(recordId);
    }
}
