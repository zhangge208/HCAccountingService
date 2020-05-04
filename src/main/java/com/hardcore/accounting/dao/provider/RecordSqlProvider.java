package com.hardcore.accounting.dao.provider;

import com.hardcore.accounting.model.persistence.Record;

import org.apache.ibatis.jdbc.SQL;

public class RecordSqlProvider {

    /**
     * Update record item via specific record.
     *
     * @param record the tag item need to update.
     * @return the sql to execute.
     */

    public String updateRecord(final Record record) {
        // Dynamic SQL
        return new SQL() {
            {
                UPDATE("hcas_record");
                if (record.getAmount() != null) {
                    SET("amount = #{amount}");
                }
                if (record.getCategory() != null) {
                    SET("category = #{category}");
                }
                if (record.getNote() != null) {
                    SET("note = #{note}");
                }
                if (record.getStatus() != null) {
                    SET("status = #{status}");
                }
                if (record.getUserId() != null) {
                    SET("user_id = #{userId}");
                }
                WHERE("id = #{id}");
            }
        }.toString();
    }
}
