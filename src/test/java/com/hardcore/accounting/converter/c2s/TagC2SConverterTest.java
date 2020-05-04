package com.hardcore.accounting.converter.c2s;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import com.hardcore.accounting.model.common.Tag;
import lombok.val;
import org.junit.jupiter.api.Test;

class TagC2SConverterTest {

    private TagC2SConverter tagC2SConverter = new TagC2SConverter();

    @Test
    void testDoForward() {
        // Arrange
        val tagId = 100L;
        val userId = 1L;
        val tagInCommon = Tag.builder()
                             .id(tagId)
                             .description("playing")
                             .userId(userId)
                             .status("ENABLE")
                             .build();
        // Act
        val result = tagC2SConverter.convert(tagInCommon);

        // Assert
        assertThat(result).isNotNull()
                          .hasFieldOrPropertyWithValue("id", tagId)
                          .hasFieldOrPropertyWithValue("userId", userId)
                          .hasFieldOrPropertyWithValue("description", "playing");
    }

    @Test
    void testDoBackward() {
        // Arrange
        val tagId = 100L;
        val userId = 1L;
        val tag = com.hardcore.accounting.model.service.Tag.builder()
                                                           .id(tagId)
                                                           .description("playing")
                                                           .userId(userId)
                                                           .status("ENABLE")
                                                           .build();
        // Act
        val result = tagC2SConverter.reverse().convert(tag);
        // Assert
        // Assert
        assertThat(result).isNotNull()
                          .hasFieldOrPropertyWithValue("id", tagId)
                          .hasFieldOrPropertyWithValue("userId", userId)
                          .hasFieldOrPropertyWithValue("description", "playing");
    }

}
