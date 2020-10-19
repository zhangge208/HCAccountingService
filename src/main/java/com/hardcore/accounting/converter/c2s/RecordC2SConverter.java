package com.hardcore.accounting.converter.c2s;

import com.hardcore.accounting.model.common.Record;
import com.hardcore.accounting.model.common.Tag;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Converter;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import javax.validation.constraints.NotNull;

@Component
@AllArgsConstructor(onConstructor_ = {@Autowired})
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class RecordC2SConverter extends Converter<Record, com.hardcore.accounting.model.service.Record> {

    private final TagC2SConverter tagC2SConverter;

    @VisibleForTesting
    public RecordC2SConverter() {
        this.tagC2SConverter = new TagC2SConverter();
    }

    @Override
    protected com.hardcore.accounting.model.service.Record doForward(@NotNull Record record) {
        val tagList = new ArrayList<com.hardcore.accounting.model.service.Tag>();
        tagC2SConverter.convertAll(record.getTagList())
                       .forEach(tagList::add);

        return com.hardcore.accounting.model.service.Record.builder()
                                                           .id(record.getId())
                                                           .note(record.getNote())
                                                           .userId(record.getUserId())
                                                           .tagList(tagList)
                                                           .category(record.getCategory())
                                                           .amount(record.getAmount())
                                                           .build();
    }

    @Override
    protected Record doBackward(@NotNull com.hardcore.accounting.model.service.Record record) {
        val recordToReturn = Record.builder()
                                   .id(record.getId())
                                   .userId(record.getUserId())
                                   .note(record.getNote())
                                   .category(record.getCategory())
                                   .amount(record.getAmount())
                                   .build();

        if (record.getTagList() != null) {
            val tagList = new ArrayList<Tag>();
            tagC2SConverter.reverse()
                           .convertAll(record.getTagList())
                           .forEach(tagList::add);
            recordToReturn.setTagList(tagList);
        }

        return recordToReturn;
    }
}
