package com.hardcore.accounting.dao;

import com.hardcore.accounting.model.persistence.RecordTagMapping;
import com.hardcore.accounting.model.persistence.Tag;

import java.util.List;

public interface RecordTagMappingDao {
    void insertRecordTagMapping(RecordTagMapping recordTagMapping);

    void batchInsertRecordTagMapping(List<Tag> tags, Long recordId);


    List<Tag> getTagListByRecordId(Long recordId);

    List<RecordTagMapping> getRecordTagMappingListByRecordId(Long recordId);

    void deleteRecordTagMappingListByRecordId(Long recordId);
}
