package com.hardcore.accounting.converter.p2s;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import com.hardcore.accounting.converter.p2c.RecordP2CConverter;
import com.hardcore.accounting.converter.p2c.TagP2CConverter;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RecordP2CConverterTest {

    private RecordP2CConverter recordP2CConverter = new RecordP2CConverter(new TagP2CConverter());

    @Test
    void testDoForward() {
        // Arrange
        val recordId = 100L;
        val userId = 1L;
        val amount = new BigDecimal("13.24");
        val createTime = LocalDateTime.now();
        val record = com.hardcore.accounting.model.persistence.Record.builder()
                                                                     .id(recordId)
                                                                     .userId(userId)
                                                                     .note("Playing game")
                                                                     .status(1)
                                                                     .amount(amount)
                                                                     .category(1)
                                                                     .tagList(ImmutableList.of(com.hardcore.accounting.model.persistence.Tag.builder()
                                                                                                                                        .id(1L)
                                                                                                                                        .status(1)
                                                                                                                                        .userId(userId)
                                                                                                                                        .description("playing")
                                                                                                                                        .build()))
                                                                     .createTime(createTime)
                                                                     .build();
        // Act
        val result = recordP2CConverter.convert(record);

        // Assert
        assertThat(result).isNotNull()
                          .hasFieldOrPropertyWithValue("id", recordId)
                          .hasFieldOrPropertyWithValue("userId", userId)
                          .hasFieldOrPropertyWithValue("note", "Playing game")
                          .hasFieldOrPropertyWithValue("category", "INCOME")
                          .hasFieldOrPropertyWithValue("tagList", ImmutableList.of(com.hardcore.accounting.model.common.Tag.builder()
                                                                                                                           .id(1L)
                                                                                                                           .status("ENABLE")
                                                                                                                           .userId(userId)
                                                                                                                           .description("playing")
                                                                                                                           .build()))
                          .hasFieldOrPropertyWithValue("amount", amount);
    }

    @Test
    void testDoBackward() {
        // Arrange
        val recordId = 100L;
        val userId = 1L;
        val amount = new BigDecimal("13.24");
        val record = com.hardcore.accounting.model.common.Record.builder()
                                                                 .id(recordId)
                                                                 .userId(userId)
                                                                 .note("Playing game")
                                                                 .amount(amount)
                                                                 .category("INCOME")
                                                                 .tagList(ImmutableList.of(com.hardcore.accounting.model.common.Tag.builder()
                                                                                                                                    .id(1L)
                                                                                                                                    .status("ENABLE")
                                                                                                                                    .userId(userId)
                                                                                                                                    .description("playing")
                                                                                                                                    .build()))
                                                                 .build();
        // Act
        val result = recordP2CConverter.reverse().convert(record);
        // Assert
        assertThat(result).isNotNull()
                          .hasFieldOrPropertyWithValue("id", recordId)
                          .hasFieldOrPropertyWithValue("userId", userId)
                          .hasFieldOrPropertyWithValue("note", "Playing game")
                          .hasFieldOrPropertyWithValue("category", 1)
                          .hasFieldOrPropertyWithValue("tagList", ImmutableList.of(com.hardcore.accounting.model.persistence.Tag.builder()
                                                                                                                                      .id(1L)
                                                                                                                                      .status(1)
                                                                                                                                      .userId(userId)
                                                                                                                                      .description("playing")
                                                                                                                                      .build()))
                          .hasFieldOrPropertyWithValue("amount", amount);
    }

}
