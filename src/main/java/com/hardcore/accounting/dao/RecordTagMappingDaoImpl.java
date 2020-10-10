package com.hardcore.accounting.dao;

import com.hardcore.accounting.dao.mapper.RecordTagMappingMapper;
import com.hardcore.accounting.model.persistence.RecordTagMapping;
import com.hardcore.accounting.model.persistence.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class RecordTagMappingDaoImpl implements RecordTagMappingDao {
    private final RecordTagMappingMapper recordTagMappingMapper;

    @Override
    public void insertRecordTagMapping(RecordTagMapping recordTagMapping) {
        log.debug("recordTagMapping in RecordTagMappingDaoImpl: {}", recordTagMapping);
        int row = recordTagMappingMapper.insertRecordTagMapping(recordTagMapping);
        log.debug("The row inserted: {}", row);
    }

    @Override
    public void batchInsertRecordTagMapping(List<Tag> tags, Long recordId) {
        //throw new RuntimeException("Throw exception after insertRecord.");

        val recordTagMappingList = tags.stream()
                                       .map(tag -> RecordTagMapping.builder()
                                                                   .status(1)
                                                                   .tagId(tag.getId())
                                                                   .recordId(recordId)
                                                                   .build())
                                       .collect(Collectors.toList());
        int rows = recordTagMappingMapper.batchRecordTagMapping(recordTagMappingList);
        log.debug("The row inserted: {}", rows);
    }

    @Override
    public List<Tag> getTagListByRecordId(Long recordId) {
        val result = recordTagMappingMapper.getTagListByRecordId(recordId);
        log.debug("result for getTagListByRecordId: {}", result);
        return result;
    }

    @Override
    public List<RecordTagMapping> getRecordTagMappingListByRecordId(Long recordId) {
        return recordTagMappingMapper.getRecordTagMappingListByRecordId(recordId);
    }

    @Override
    public void deleteRecordTagMappingListByRecordId(Long recordId) {
        int rows = recordTagMappingMapper.deleteRecordTagMappingList(recordId);
        log.debug("The affected rows for deleting: {}", rows);
    }
}
