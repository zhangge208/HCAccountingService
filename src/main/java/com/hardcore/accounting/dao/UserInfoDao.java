package com.hardcore.accounting.dao;

import com.hardcore.accounting.model.persistence.UserInfo;

public interface UserInfoDao {

    UserInfo getUserInfoById(Long id);

    void createNewUser(String username, String password);
}
