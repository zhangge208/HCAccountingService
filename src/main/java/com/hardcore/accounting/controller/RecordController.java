package com.hardcore.accounting.controller;

import com.hardcore.accounting.converter.c2s.RecordC2SConverter;
import com.hardcore.accounting.exception.InvalidParameterException;
import com.hardcore.accounting.manager.RecordManager;
import com.hardcore.accounting.manager.UserInfoManager;
import com.hardcore.accounting.model.service.Record;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1.0/records")
@Slf4j
public class RecordController {
    private final RecordManager recordManager;
    private final RecordC2SConverter recordC2SConverter;
    private final UserInfoManager userInfoManager;

    /**
     * The constructor for RecordController.
     * @param recordManager record manager
     * @param recordC2SConverter record converter
     * @param userInfoManager user info manager
     */
    @Autowired
    public RecordController(final RecordManager recordManager,
                            final RecordC2SConverter recordC2SConverter,
                            final UserInfoManager userInfoManager) {
        this.recordManager = recordManager;
        this.recordC2SConverter = recordC2SConverter;
        this.userInfoManager = userInfoManager;
    }

    /**
     * Create record with related information.
     * @param record record information to create.
     * @return the created record
     */
    @PostMapping(produces = "application/json", consumes = "application/json")
    public Record createRecord(@RequestBody Record record) {
        if (checkRecord(record)) {
            throw new InvalidParameterException("invalid record to created");
        }
        val recordInCommon = recordC2SConverter.reverse().convert(record);
        val resource = recordManager.createRecord(recordInCommon);
        return recordC2SConverter.convert(resource);
    }

    private boolean checkRecord(@RequestBody Record record) {
        return record.getTagList() == null
               || record.getAmount() == null
               || record.getUserId() == null
               || record.getCategory() == null;
    }

    /**
     * Get tag information by tag id.
     * @param recordId the specific record id.
     * @return The related tag information
     */
    @GetMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    public Record getRecordByRecordId(@PathVariable("id") Long recordId) {
        val record = recordManager.getRecordByRecordId(recordId);
        return recordC2SConverter.convert(record);
    }

    /**
     * Update record information for specific tag.
     * @param recordId the specific record id
     * @param record the record information
     * @return the updated record information
     */
    @PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    public Record updateRecord(@PathVariable("id") Long recordId, @RequestBody Record record) {
        if (recordId == null || recordId <= 0L) {
            throw new InvalidParameterException("The record id must be not empty and positive.");
        }

        if (record.getUserId() == null || record.getUserId() <= 0L) {
            throw new InvalidParameterException("The user id is empty or invalid");
        }

        userInfoManager.getUserInfoByUserId(record.getUserId()); // cache.get(user_id) bitmap bloomfilter
        record.setId(recordId);
        val recordInCommon = recordC2SConverter.reverse().convert(record);
        val resource = recordManager.updateRecord(recordInCommon);
        return recordC2SConverter.convert(resource);
    }
}
