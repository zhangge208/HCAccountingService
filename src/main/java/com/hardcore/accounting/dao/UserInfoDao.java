package com.hardcore.accounting.dao;

import com.hardcore.accounting.model.persistence.UserInfo;

public interface UserInfoDao {

    UserInfo getUserInfoById(Long id);

    UserInfo getUserInfoByUserName(String username);

    void createNewUser(UserInfo userInfo);
}
