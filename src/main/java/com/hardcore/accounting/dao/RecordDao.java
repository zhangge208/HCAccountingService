package com.hardcore.accounting.dao;

import com.hardcore.accounting.model.persistence.Record;

public interface RecordDao {
    void insertRecord(Record record);

    void updateRecord(Record record);

    Record getRecordByRecordId(Long id);
}
