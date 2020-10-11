package com.hardcore.accounting.manager;

import com.hardcore.accounting.converter.p2c.RecordP2CConverter;
import com.hardcore.accounting.dao.RecordDao;
import com.hardcore.accounting.dao.RecordTagMappingDao;
import com.hardcore.accounting.dao.TagDao;
import com.hardcore.accounting.exception.InvalidParameterException;
import com.hardcore.accounting.exception.ResourceNotFoundException;
import com.hardcore.accounting.model.common.Record;
import com.hardcore.accounting.model.persistence.Tag;

import lombok.val;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RecordManagerImpl implements RecordManager {
    private final RecordDao recordDao;
    private final TagDao tagDao;
    private final RecordTagMappingDao recordTagMappingDao;
    private final RecordP2CConverter recordP2CConverter;


    /**
     * The constructor for RecordManagerImpl.
     *
     * @param recordDao           record dao
     * @param tagDao              tag dao
     * @param recordTagMappingDao record tag mapping dao
     * @param recordP2CConverter  record converter
     */
    public RecordManagerImpl(final RecordDao recordDao,
                             final TagDao tagDao,
                             final RecordTagMappingDao recordTagMappingDao,
                             final RecordP2CConverter recordP2CConverter) {
        this.recordDao = recordDao;
        this.tagDao = tagDao;
        this.recordTagMappingDao = recordTagMappingDao;
        this.recordP2CConverter = recordP2CConverter;
    }

    @Override
    @Transactional
    public Record createRecord(Record record) {
        val newRecord = recordP2CConverter.reverse().convert(record);

        // check tag list are valid
        assert newRecord != null;
        val tagIds = newRecord.getTagList()
                              .stream()
                              .map(Tag::getId)
                              .collect(Collectors.toList());
        val tags = tagDao.getTagListByIds(tagIds);
        if (tags.isEmpty()) {
            throw new InvalidParameterException(String.format("The tag list %s are not existed.", tagIds));
        }
        tags.forEach(tag -> {
            if (!tag.getUserId().equals(record.getUserId())) {
                throw new InvalidParameterException("The tag is not matched for user");
            }

            /*
             val recordTagMapping = RecordTagMapping.builder()
             .recordId(newRecord.getId())
             .tagId(tag.getId())
             .status(1)
             .build();
             recordTagMappingDao.insertRecordTagMapping(recordTagMapping); // mysql request io each */
        });
        recordDao.insertRecord(newRecord);
        recordTagMappingDao.batchInsertRecordTagMapping(tags, newRecord.getId());
        return getRecordByRecordId(newRecord.getId());
    }

    @Override
    @Cacheable(value = "record", key = "#recordId")
    public Record getRecordByRecordId(Long recordId) {
        return Optional.ofNullable(recordDao.getRecordByRecordId(recordId))
                       .map(recordP2CConverter::convert)
                       .orElseThrow(() -> new ResourceNotFoundException(
                           String.format("The related record [%s] was not found.", recordId)));
    }

    @Override
    @Transactional
    @CacheEvict(value = "record", key = "#record.id")
    public Record updateRecord(Record record) {
        val updateRecord = recordP2CConverter.reverse().convert(record);
        val existingRecord = Optional.ofNullable(recordDao.getRecordByRecordId(record.getId()))
                                     .orElseThrow(() -> new ResourceNotFoundException(
                                         String.format("The related record id [%s] was not found", record.getId())));
        if (!record.getUserId().equals(existingRecord.getUserId())) {
            throw new InvalidParameterException(
                String.format("The record id [%s] doesn't belong to user id: [%s]",
                              record.getId(), record.getUserId()));
        }

        assert updateRecord != null;
        if (updateRecord.getTagList() != null && !updateRecord.getTagList().equals(existingRecord.getTagList())) {
            val tagIds = updateRecord.getTagList()
                                     .stream()
                                     .map(Tag::getId)
                                     .collect(Collectors.toList());
            // Check tag is valid
            val tags = tagDao.getTagListByIds(tagIds);
            tags.stream()
                .filter(tag -> !tag.getUserId().equals(record.getUserId()))
                .findAny()
                .ifPresent(tag -> {
                    throw new InvalidParameterException(
                        String.format("The tags id [%s] doesn't belong to user id: [%s]",
                                      tag.getId(), record.getUserId()));
                });
            //Deleting the existing mappings
            recordTagMappingDao.deleteRecordTagMappingListByRecordId(record.getId());
            // Creating new mappings
            recordTagMappingDao.batchInsertRecordTagMapping(tags, record.getId());
        }

        recordDao.updateRecord(updateRecord);
        return getRecordByRecordId(record.getId());
    }

    @Override
    public List<Record> getAllRecords() {
        return null;
    }
}
