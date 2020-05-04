package com.hardcore.accounting.dao.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.hardcore.accounting.model.persistence.Record;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class RecordSqlProviderTest {
    private RecordSqlProvider recordSqlProvider = new RecordSqlProvider();
    @Test
    void testUpdateRecord() {
        // Arrange
        val record = Record.builder()
                           .id(1L)
                           .amount(new BigDecimal("100.05"))
                           .status(0)
                           .build();

        // Act
        val result = recordSqlProvider.updateRecord(record);
        String expectedSql = "UPDATE hcas_record\n"
                             + "SET amount = #{amount}, status = #{status}\n"
                             + "WHERE (id = #{id})";
        assertEquals(expectedSql, result);
    }
}
