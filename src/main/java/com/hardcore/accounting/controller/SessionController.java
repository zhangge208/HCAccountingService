package com.hardcore.accounting.controller;

import com.hardcore.accounting.manager.UserInfoManager;
import com.hardcore.accounting.model.service.UserInfo;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("v1.0/session")
public class SessionController {

    private static final String SUCCESS = "success";
    private static final String STATUS = "status";
    private static final String USERNAME = "username";
    private final UserInfoManager userInfoManager;

    @Autowired
    public SessionController(UserInfoManager userInfoManager) {
        this.userInfoManager = userInfoManager;
    }

    /**
     * Login with username and password.
     * @param userInfo the specific user information.
     * @return The response for login
     */
    @PostMapping(produces = "application/json", consumes = "application/json")
    public Map<String, String> login(@RequestBody UserInfo userInfo) {
        val response = new HashMap<String, String>();
        userInfoManager.login(userInfo.getUsername(), userInfo.getPassword());
        response.put(STATUS, SUCCESS);
        response.put(USERNAME, userInfo.getUsername());
        return response;
    }

}
