package com.hardcore.accounting.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.hardcore.accounting.converter.p2c.TagP2CConverter;
import com.hardcore.accounting.dao.TagDao;
import com.hardcore.accounting.exception.ResourceNotFoundException;
import com.hardcore.accounting.model.common.Tag;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TagManagerTest {
    @Mock
    private TagDao tagDao;
    @Spy
    private TagP2CConverter tagP2CConverter;

    @InjectMocks
    private TagManagerImpl tagManager;

    @Test
    void testCreateTag() {
        // Arrange
        val tagId = 100L;
        val userId = 1900L;
        val description = "playing";
        val tag = Tag.builder()
                             .id(tagId)
                             .description(description)
                             .userId(userId)
                             .status("ENABLE")
                             .build();
        val tagInDb = com.hardcore.accounting.model.persistence.Tag.builder()
                                                                   .id(tagId)
                                                                   .description(description)
                                                                   .userId(userId)
                                                                   .status(1)
                                                                   .build();
        doReturn(null).when(tagDao).getTagByDescription(description, userId);
        doNothing().when(tagDao).createNewTag(any(com.hardcore.accounting.model.persistence.Tag.class));
        // Act
        val result = tagManager.createTag(description, userId);
        // Assert
        verify(tagDao).getTagByDescription(description, userId);
        verify(tagDao).createNewTag(any(com.hardcore.accounting.model.persistence.Tag.class));
    }

    @Test
    void testUpdateTag() {
        // Arrange
        val tagId = 100L;
        val userId = 1900L;
        val description = "playing";
        val tag = Tag.builder()
                     .id(tagId)
                     .description(description)
                     .userId(userId)
                     .status("ENABLE")
                     .build();
        val tagInDb = com.hardcore.accounting.model.persistence.Tag.builder()
                                                                   .id(tagId)
                                                                   .description(description)
                                                                   .userId(userId)
                                                                   .status(1)
                                                                   .build();
        doReturn(tagInDb).when(tagDao).getTagByTagId(tagId);
        doNothing().when(tagDao).updateTag(tagInDb);
        // Act
        val result = tagManager.updateTag(tag);
        // Assert
        assertEquals(tag, result);
        verify(tagDao, times(2)).getTagByTagId(tagId);
        verify(tagDao).updateTag(any(com.hardcore.accounting.model.persistence.Tag.class));
    }

    @Test
    void testGetTagByTagId() {
        // Arrange
        val tagId = 100L;
        val userId = 1900L;
        val description = "playing";
        val tag = Tag.builder()
                     .id(tagId)
                     .description(description)
                     .userId(userId)
                     .status("ENABLE")
                     .build();

        val tagInDb = com.hardcore.accounting.model.persistence.Tag.builder()
                                                                   .id(tagId)
                                                                   .description(description)
                                                                   .userId(userId)
                                                                   .status(1)
                                                                   .build();
        doReturn(tagInDb).when(tagDao).getTagByTagId(tagId);
        // Act
        val result = tagManager.getTagByTagId(tagId);
        // Assert
        assertEquals(tag, result);
        verify(tagDao).getTagByTagId(tagId);
    }

    @Test
    void testGetTagByInvalidTagId() {
        // Arrange
        val tagId = 100L;
        doReturn(null).when(tagDao).getTagByTagId(tagId);
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> tagManager.getTagByTagId(tagId));
        verify(tagDao).getTagByTagId(tagId);
    }
}
