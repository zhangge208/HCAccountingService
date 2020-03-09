package com.hardcore.accounting.manager;


import com.hardcore.accounting.converter.p2c.UserInfoP2CConverter;
import com.hardcore.accounting.dao.UserInfoDAO;
import com.hardcore.accounting.model.common.UserInfo;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserInfoManagerImpl implements UserInfoManager {

    private final UserInfoDAO userInfoDAO;
    private final UserInfoP2CConverter userInfoP2CConverter;

    @Autowired
    public UserInfoManagerImpl(final UserInfoDAO userInfoDAO,
                               final UserInfoP2CConverter userInfoP2CConverter) {
        this.userInfoDAO = userInfoDAO;
        this.userInfoP2CConverter = userInfoP2CConverter;
    }

    @Override
    public UserInfo getUserInfoByUserId(Long userId) {
        val userInfo = userInfoDAO.getUserInfoById(userId);
        return userInfoP2CConverter.convert(userInfo);
    }
}
