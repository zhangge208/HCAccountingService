package com.hardcore.accounting.controller;

import com.hardcore.accounting.converter.c2s.UserInfoC2SConverter;
import com.hardcore.accounting.exception.InvalidParameterException;
import com.hardcore.accounting.manager.UserInfoManager;
import com.hardcore.accounting.model.service.UserInfo;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("v1.0/users")
@Slf4j
public class UserController {

    private final UserInfoManager userInfoManager;
    private final UserInfoC2SConverter userInfoC2SConverter;

    @Autowired
    public UserController(final UserInfoManager userInfoManager,
                          final UserInfoC2SConverter userInfoC2SConverter) {
        this.userInfoManager = userInfoManager;
        this.userInfoC2SConverter = userInfoC2SConverter;
    }

    /**
     * Get user information by specific user id.
     * @param userId the user id
     * @return user info response entity.
     */
    @GetMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<UserInfo> getUserInfoByUserId(@PathVariable("id") @NotNull Long userId) {
        log.debug("Get user info by user id {}", userId);
        if (userId <= 0L) {
            throw new InvalidParameterException(String.format("The user id %s is invalid", userId));
        }
        val userInfo = userInfoManager.getUserInfoByUserId(userId);
        val userInfoToReturn = userInfoC2SConverter.convert(userInfo);
        assert userInfoToReturn != null;
        return ResponseEntity.ok(userInfoToReturn);

    }

    /**
     * Register with username and password.
     * @param userInfo userInfo
     * @return The response for register
     */
    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<UserInfo> register(@RequestBody UserInfo userInfo) {
        val userInfoToReturn = userInfoC2SConverter.convert(
            userInfoManager.register(userInfo.getUsername(), userInfo.getPassword()));
        assert userInfoToReturn != null;
        return ResponseEntity.ok(userInfoToReturn);
    }
}
