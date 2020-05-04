package com.hardcore.accounting.converter.c2s;

import com.hardcore.accounting.model.common.Tag;

import com.google.common.base.Converter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TagC2SConverter extends Converter<Tag, com.hardcore.accounting.model.service.Tag> {

    @Override
    protected com.hardcore.accounting.model.service.Tag doForward(Tag tag) {
        return com.hardcore.accounting.model.service.Tag.builder()
                                                        .description(tag.getDescription())
                                                        .id(tag.getId())
                                                        .userId(tag.getUserId())
                                                        .status(tag.getStatus())
                                                        .build();
    }

    @Override
    protected Tag doBackward(com.hardcore.accounting.model.service.Tag tag) {
        return Tag.builder()
                  .id(tag.getId())
                  .description(tag.getDescription())
                  .userId(tag.getUserId())
                  .status(tag.getStatus())
                  .build();
    }
}
