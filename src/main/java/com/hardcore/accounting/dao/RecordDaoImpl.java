package com.hardcore.accounting.dao;

import com.hardcore.accounting.dao.mapper.RecordMapper;
import com.hardcore.accounting.model.persistence.Record;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class RecordDaoImpl implements RecordDao {
    private final RecordMapper recordMapper;

    @Override
    public void insertRecord(Record record) {
        record.setStatus(1);
        log.debug("Record in RecordDaoImpl: {}", record);
        recordMapper.insertRecord(record);
        log.debug("Record in RecordDaoImpl after inserting: {}", record);
    }

    @Override
    public void updateRecord(Record record) {
        recordMapper.updateRecord(record);
    }

    @Override
    public Record getRecordByRecordId(Long id) {
        val result = recordMapper.getRecordByRecordId(id);
        log.debug("Record in getRecordByRecordId: {}", result);
        return result;
    }

    @Override
    public List<Record> getRecords() {
        return null;
    }
}
