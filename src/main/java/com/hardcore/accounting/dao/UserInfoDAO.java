package com.hardcore.accounting.dao;

import com.hardcore.accounting.model.persistence.UserInfo;

public interface UserInfoDAO {

    UserInfo getUserInfoById(Long id);

    void createNewUser(String username, String password);


}
