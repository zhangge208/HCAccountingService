package com.hardcore.accounting.manager;

import com.hardcore.accounting.model.common.UserInfo;

public interface UserInfoManager {
    /**
     * Get user information by user id.
     * @param userId the specific user id.
     */
    UserInfo getUserInfoByUserId(Long userId);
}
