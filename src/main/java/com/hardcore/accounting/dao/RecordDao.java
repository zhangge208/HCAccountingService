package com.hardcore.accounting.dao;

import com.hardcore.accounting.model.persistence.Record;

import java.util.List;

public interface RecordDao {
    void insertRecord(Record record);

    void updateRecord(Record record);

    Record getRecordByRecordId(Long id);

    List<Record> getRecords();
}
