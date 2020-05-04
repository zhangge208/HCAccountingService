package com.hardcore.accounting.converter.p2s;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import com.hardcore.accounting.converter.p2c.TagP2CConverter;
import com.hardcore.accounting.model.persistence.Tag;
import lombok.val;
import org.junit.jupiter.api.Test;

public class TagP2CConverterTest {

    private TagP2CConverter tagP2CConverter = new TagP2CConverter();

    @Test
    void testDoForward() {
        // Arrange
        val tagId = 10L;
        val userId = 100L;
        val description = "playing";
        val createTime = LocalDateTime.now();

        val tagInCommon = Tag.builder()
                             .id(tagId)
                             .description("playing")
                             .userId(userId)
                             .status(1)
                             .createTime(createTime)
                             .build();
        // Act
        val result = tagP2CConverter.convert(tagInCommon);

        // Assert
        assertThat(result).isNotNull()
                          .hasFieldOrPropertyWithValue("id", tagId)
                          .hasFieldOrPropertyWithValue("description", description)
                          .hasFieldOrPropertyWithValue("userId", userId)
                          .hasFieldOrPropertyWithValue("status", "ENABLE");
    }

    @Test
    void testDoBackward() {
        // Arrange
        val userId = 100L;
        val description = "playing";
        val password = "hardcore";

        val tag = com.hardcore.accounting.model.common.Tag.builder()
                                                          .id(userId)
                                                          .description(description)
                                                          .userId(userId)
                                                          .status("ENABLE")
                                                          .build();

        // Act
        val result = tagP2CConverter.reverse().convert(tag);
        // Assert
        assertThat(result).isNotNull()
                          .hasFieldOrPropertyWithValue("id", userId)
                          .hasFieldOrPropertyWithValue("description", description)
                          .hasFieldOrPropertyWithValue("userId", userId)
                          .hasFieldOrPropertyWithValue("status", 1);

    }

}
