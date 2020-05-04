package com.hardcore.accounting.dao;

import com.hardcore.accounting.dao.mapper.TagMapper;
import com.hardcore.accounting.model.persistence.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TagDaoImpl implements TagDao {

    private final TagMapper tagMapper;

    @Override
    public Tag getTagByTagId(Long id) {
        val tag = tagMapper.getTagByTagId(id);
        log.debug("The tag item from db: {}", tag);
        return tag;
    }

    @Override
    public Tag getTagByDescription(String description, Long userId) {
        return tagMapper.getTagByDescription(description, userId);
    }

    @Override
    public void createNewTag(Tag tag) {
        tagMapper.insertTag(tag);
    }

    @Override
    public void updateTag(Tag tag) {
        tagMapper.updateTag(tag);
    }

    @Override
    public List<Tag> getTagListByIds(List<Long> ids) {
        return tagMapper.getTagListByIds(ids);
    }
}
