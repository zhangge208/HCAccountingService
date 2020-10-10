package com.hardcore.accounting.manager;

import com.hardcore.accounting.converter.p2c.TagP2CConverter;
import com.hardcore.accounting.dao.TagDao;
import com.hardcore.accounting.exception.InvalidParameterException;
import com.hardcore.accounting.exception.ResourceNotFoundException;
import com.hardcore.accounting.model.common.Tag;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.ImmutableList;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TagManagerImpl implements TagManager {
    private static final int ENABLE_STATUS = 1;
    private final TagDao tagDao;
    private final TagP2CConverter tagP2CConverter;

    @Autowired
    public TagManagerImpl(final TagDao tagDao,
                          final TagP2CConverter tagP2CConverter) {
        this.tagDao = tagDao;
        this.tagP2CConverter = tagP2CConverter;
    }

    @Override
    public Tag createTag(String description, Long userId) {
        Optional.ofNullable(tagDao.getTagByDescription(description, userId))
                .ifPresent((tag) -> {
                    throw new InvalidParameterException(
                        String.format("The related tag with description [%s] has been created", description));
                });

        val newTag = com.hardcore.accounting.model.persistence.Tag.builder()
                                                                  .description(description)
                                                                  .userId(userId)
                                                                  .status(ENABLE_STATUS)
                                                                  .build();
        tagDao.createNewTag(newTag);
        return tagP2CConverter.convert(newTag);
    }

    @Override
    public Tag getTagByTagId(Long tagId) {
        return Optional.ofNullable(tagDao.getTagByTagId(tagId))
                       .map(tagP2CConverter::convert)
                       .orElseThrow(() -> new ResourceNotFoundException(
                           String.format("The related tag id [%s] was not found", tagId)));
    }

    @Override
    public Tag updateTag(Tag tag) {
        val updatingTag = tagP2CConverter.reverse().convert(tag);
        val tagInDb = Optional.ofNullable(tagDao.getTagByTagId(tag.getId()))
                              .orElseThrow(() -> new ResourceNotFoundException(
                                  String.format("The related tag id [%s] was not found", tag.getId())));

        if (!tag.getUserId().equals(tagInDb.getUserId())) {
            throw new InvalidParameterException(
                String.format("The tag id [%s] doesn't belong to user id: [%s]",
                              tag.getId(), tag.getUserId()));
        }

        tagDao.updateTag(updatingTag);
        return getTagByTagId(tag.getId());
    }

    @Override
    public PageInfo<Tag> getTags(Long userId, int pageNum, int pageSize) {
        return new PageInfo<>(
            ImmutableList.copyOf(tagP2CConverter.convertAll(tagDao.getTags(userId, pageNum, pageSize))));
    }
}
