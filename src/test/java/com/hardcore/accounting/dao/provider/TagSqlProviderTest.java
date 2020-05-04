package com.hardcore.accounting.dao.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.ImmutableList;
import com.hardcore.accounting.model.persistence.Tag;
import lombok.val;
import org.junit.jupiter.api.Test;

public class TagSqlProviderTest {

    private TagSqlProvider tagSqlProvider = new TagSqlProvider();
    @Test
    void testUpdateTagSQL() {
        // Arrange
        val description = "eating";
        Integer status = 1;
        Long userId = 10L;
        val tag = Tag.builder()
                     .description(description)
                     .status(status)
                     .userId(userId)
                     .build();

        // Act
        val result = tagSqlProvider.updateTag(tag);
        // Assert
        String expectedSql = "UPDATE hcas_tag\n"
                             + "SET description = #{description}, status = #{status}, user_id = #{userId}\n"
                             + "WHERE (id = #{id})";
        assertEquals(expectedSql, result);
    }

    @Test
    void testGetTagListByIds() {
        // Arrange
        val tagIdList = ImmutableList.of(1L, 10L, 20L, 30L);

        // Act
        val result = tagSqlProvider.getTagListByIds(tagIdList);
        // Assert
        String expectedSql = "SELECT id, description, user_id, status\n"
                             + "FROM hcas_tag\n"
                             + "WHERE (id in ('1','10','20','30'))";

        assertEquals(expectedSql, result);
    }

}
