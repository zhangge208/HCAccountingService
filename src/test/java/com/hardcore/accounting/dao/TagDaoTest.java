package com.hardcore.accounting.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.google.common.collect.ImmutableList;
import com.hardcore.accounting.dao.mapper.TagMapper;
import com.hardcore.accounting.model.persistence.Tag;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class TagDaoTest {

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagDaoImpl tagDao;

    @Test
    void testGetTagByTagId() {
        // Arrange
        val tagId = 1000L;
        val userId = 100L;
        val description = "playing";
        val createTime = LocalDateTime.now();

        val tag = Tag.builder()
                     .id(tagId)
                     .userId(userId)
                     .description(description)
                     .status(1)
                     .createTime(createTime)
                     .build();
        doReturn(tag).when(tagMapper).getTagByTagId(userId);
        // Act
        val result = tagDao.getTagByTagId(userId);
        // Assert
        assertEquals(tag, result);
        verify(tagMapper).getTagByTagId(userId);
    }

    @Test
    public void testCreateTag() {
        // Arrange
        val tagId = 1000L;
        val userId = 100L;
        val description = "playing";
        val createTime = LocalDateTime.now();

        val tag = Tag.builder()
                     .id(tagId)
                     .userId(userId)
                     .description(description)
                     .status(1)
                     .createTime(createTime)
                     .build();
        doReturn(1).when(tagMapper).insertTag(tag);
        // Act
        tagDao.createNewTag(tag);
        // Assert
        //assertEquals(userInfo, result);
        verify(tagMapper).insertTag(any(Tag.class));
    }

    @Test
    void testGetTagByDescription() {
        // Arrange
        val tagId = 1000L;
        val userId = 100L;
        val description = "playing";
        val createTime = LocalDateTime.now();

        val tag = Tag.builder()
                     .id(tagId)
                     .userId(userId)
                     .description(description)
                     .status(1)
                     .createTime(createTime)
                     .build();

        doReturn(tag).when(tagMapper).getTagByDescription(description, userId);
        // Act
        val result = tagDao.getTagByDescription(description, userId);
        // Assert
        assertEquals(tag, result);
        verify(tagMapper).getTagByDescription(description, userId);

    }

    @Test
    void testUpdateTag() {
        // Arrange
        val tagId = 1000L;
        val userId = 100L;
        val description = "playing";
        val createTime = LocalDateTime.now();

        val tag = Tag.builder()
                     .id(tagId)
                     .userId(userId)
                     .description(description)
                     .status(1)
                     .createTime(createTime)
                     .build();

        doReturn(1).when(tagMapper).updateTag(tag);
        // Act
        tagDao.updateTag(tag);
        // Assert
        verify(tagMapper).updateTag(tag);
    }

    @Test
    void testGetTagListByIds() {
        // Arrange
        val firstTagId = 10L;
        val secondTagId = 10L;
        val userId = 100L;
        val tagIdList = ImmutableList.of(firstTagId, secondTagId);

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

        doReturn(ImmutableList.of(tag1, tag2)).when(tagMapper).getTagListByIds(tagIdList);
        // Act
        val result = tagDao.getTagListByIds(tagIdList);
        // Assert
        assertThat(result).isNotNull()
                          .hasSize(2);

        verify(tagMapper).getTagListByIds(anyList());
    }
}
