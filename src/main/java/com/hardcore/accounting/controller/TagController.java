package com.hardcore.accounting.controller;

import com.hardcore.accounting.converter.c2s.TagC2SConverter;
import com.hardcore.accounting.exception.InvalidParameterException;
import com.hardcore.accounting.manager.TagManager;
import com.hardcore.accounting.manager.UserInfoManager;
import com.hardcore.accounting.model.service.Tag;

import com.github.pagehelper.PageInfo;
import lombok.val;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1.0/tags")
public class TagController {

    private final TagManager tagManager;
    private final UserInfoManager userInfoManager;
    private final TagC2SConverter tagC2SConverter;

    /**
     * Constructor for TagController.
     *
     * @param tagManager      tag manager
     * @param userInfoManager user info manager
     * @param tagC2SConverter tag converter
     */
    public TagController(final TagManager tagManager,
                         final UserInfoManager userInfoManager,
                         final TagC2SConverter tagC2SConverter) {
        this.tagManager = tagManager;
        this.tagC2SConverter = tagC2SConverter;
        this.userInfoManager = userInfoManager;
    }

    /**
     * Create tag with related information.
     *
     * @param tag tag information to create.
     * @return the created tag
     */
    @PostMapping(produces = "application/json", consumes = "application/json")
    public Tag createTag(@RequestBody Tag tag) {
        if (tag.getDescription() == null || tag.getDescription().isEmpty() || tag.getUserId() == null) {
            throw new InvalidParameterException("The description and user id must be not null or empty.");
        }
        val resource = tagManager.createTag(tag.getDescription(), tag.getUserId());
        return tagC2SConverter.convert(resource);
    }

    /**
     * Get tag information by tag id.
     *
     * @param tagId the specific tag id.
     * @return The related tag information
     */
    @GetMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    public Tag getTagByTagId(@PathVariable("id") Long tagId) {
        if (tagId == null || tagId <= 0L) {
            throw new InvalidParameterException("The tagId must be not empty and positive.");
        }
        val tag = tagManager.getTagByTagId(tagId);
        return tagC2SConverter.convert(tag);
    }

    /**
     * Update tag information for specific tag.
     *
     * @param tagId the specific tag id
     * @param tag   the tag information
     * @return the updated tag information
     */
    @PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    public Tag updateTag(@PathVariable("id") Long tagId, @RequestBody Tag tag) {
        if (tagId == null || tagId <= 0L) {
            throw new InvalidParameterException("The tagId must be not empty and positive.");
        }

        if (tag.getUserId() == null || tag.getUserId() <= 0L) {
            throw new InvalidParameterException("The userId is empty or invalid");
        }

        userInfoManager.getUserInfoByUserId(tag.getUserId());
        String status = tag.getStatus();
        if (status != null && !"ENABLE".equals(status) && !"DISABLE".equals(status)) {
            throw new InvalidParameterException(String.format("The status [%s] to update is invalid status", status));
        }
        tag.setId(tagId);

        val tagInCommon = tagC2SConverter.reverse().convert(tag);
        val resource = tagManager.updateTag(tagInCommon);
        return tagC2SConverter.convert(resource);
    }

    /**
     * Get tags with specific page num and size.
     * @param pageNum page num
     * @param pageSize page size
     * @return The tag list
     */

    @GetMapping(produces = "application/json", consumes = "application/json")
    public PageInfo<com.hardcore.accounting.model.common.Tag> getTags(@RequestParam("pageNum") int pageNum,
                                                                         @RequestParam("pageSize") int pageSize) {
        val username = (String) SecurityUtils.getSubject().getPrincipal();
        val userInfo = userInfoManager.getUserInfoByUserName(username);
        return tagManager.getTags(userInfo.getId(), pageNum, pageSize);
    }
}
