package com.hardcore.accounting.dao;

import com.hardcore.accounting.model.persistence.Tag;

import java.util.List;

public interface TagDao {
    Tag getTagByTagId(Long id);

    Tag getTagByDescription(String description, Long userId);

    void createNewTag(Tag tag);

    void updateTag(Tag tag);

    List<Tag> getTagListByIds(List<Long> ids);

    List<Tag> getTags(Long userId, int pageNum, int pageSize);
}
