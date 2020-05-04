package com.hardcore.accounting.manager;

import com.hardcore.accounting.model.common.UserInfo;

public interface UserInfoManager {
    /**
     * Get user information by user id.
     * @param userId the specific user id.
     */
    UserInfo getUserInfoByUserId(Long userId);

    /**
     * Get user information by user name.
     * @param username the specific user name.
     */
    UserInfo getUserInfoByUserName(String username);

    /**
     * Login with username and password.
     * @param username username
     * @param password the related password
     */
    void login(String username, String password);

    /**
     * Register new user with username and password.
     * @param username username
     * @param password the related password
     */
    UserInfo register(String username, String password);

}
