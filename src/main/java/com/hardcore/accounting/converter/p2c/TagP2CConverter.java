package com.hardcore.accounting.converter.p2c;

import com.hardcore.accounting.model.persistence.Tag;

import com.google.common.base.Converter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TagP2CConverter extends Converter<Tag, com.hardcore.accounting.model.common.Tag> {

    private static final String ENABLE = "ENABLE";
    private static final String DISABLE = "DISABLE";

    @Override
    protected com.hardcore.accounting.model.common.Tag doForward(@NotNull Tag tag) {
        return com.hardcore.accounting.model.common.Tag.builder()
                                                       .id(tag.getId())
                                                       .description(tag.getDescription())
                                                       .status(tag.getStatus() == 1 ? ENABLE : DISABLE)
                                                       .userId(tag.getUserId())
                                                       .build();
    }

    @Override
    protected Tag doBackward(com.hardcore.accounting.model.common.Tag tag) {

        val tagInDb = Tag.builder()
                         .id(tag.getId())
                         .description(tag.getDescription())
                         .userId(tag.getUserId())
                         .build();

        if (tag.getStatus() != null) {
            tagInDb.setStatus(ENABLE.equals(tag.getStatus()) ? 1 : 0);
        }

        return tagInDb;
    }
}
