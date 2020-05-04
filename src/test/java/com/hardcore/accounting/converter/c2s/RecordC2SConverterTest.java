package com.hardcore.accounting.converter.c2s;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import com.google.common.collect.ImmutableList;
import com.hardcore.accounting.model.common.Record;
import com.hardcore.accounting.model.common.Tag;
import lombok.val;
import org.junit.jupiter.api.Test;

class RecordC2SConverterTest {

    private RecordC2SConverter recordC2SConverter = new RecordC2SConverter(new TagC2SConverter());

    @Test
    void testDoForward() {
        // Arrange
        val recordId = 100L;
        val userId = 1L;
        val amount = new BigDecimal("13.24");
        val recordInCommon = Record.builder()
                                   .id(recordId)
                                   .status("ENABLE")
                                   .userId(userId)
                                   .note("Playing game")
                                   .amount(amount)
                                   .category("INCOME")
                                   .tagList(ImmutableList.of(Tag.builder()
                                                                .id(1L)
                                                                .description("playing")
                                                                .userId(userId)
                                                                .status("ENABLE")
                                                                .build()))
                                   .build();
        // Act
        val result = recordC2SConverter.convert(recordInCommon);

        // Assert
        assertThat(result).isNotNull()
                          .hasFieldOrPropertyWithValue("id", recordId)
                          .hasFieldOrPropertyWithValue("userId", userId)
                          .hasFieldOrPropertyWithValue("note", "Playing game")
                          .hasFieldOrPropertyWithValue("category", "INCOME")
                          .hasFieldOrPropertyWithValue("tagList", ImmutableList.of(com.hardcore.accounting.model.service.Tag.builder()
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
        val record = com.hardcore.accounting.model.service.Record.builder()
                                                                 .id(recordId)
                                                                 .userId(userId)
                                                                 .note("Playing game")
                                                                 .amount(amount)
                                                                 .category("INCOME")
                                                                 .tagList(ImmutableList.of(com.hardcore.accounting.model.service.Tag.builder()
                                                                                                                                    .id(1L)
                                                                                                                                    .status("ENABLE")
                                                                                                                                    .userId(userId)
                                                                                                                                    .description("playing")
                                                                                                                                    .build()))
                                                                 .build();
        // Act
        val result = recordC2SConverter.reverse().convert(record);
        // Assert
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

}
