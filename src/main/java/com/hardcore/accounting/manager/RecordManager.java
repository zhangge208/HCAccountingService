package com.hardcore.accounting.manager;

import com.hardcore.accounting.model.common.Record;

import java.util.List;

/**
 * The inferface for record manager.
 */
public interface RecordManager {

    /**
     * Create new record with specific information.
     * @param record the specific record
     * @return the created record
     */
    Record createRecord(Record record);

    /**
     * Get record with specific record id.
     * @param recordId the specific record id.
     * @return the related record
     */
    Record getRecordByRecordId(Long recordId);

    /**
     * Update record with specific record information.
     * @param record the specific record.
     * @return the updated record
     */
    Record updateRecord(Record record);


    List<Record> getAllRecords();

}
