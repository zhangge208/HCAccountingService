package com.hardcore.accounting.manager;

import com.hardcore.accounting.converter.p2c.UserInfoP2CConverter;
import com.hardcore.accounting.dao.UserInfoDao;
import com.hardcore.accounting.exception.ResourceNotFoundException;
import com.hardcore.accounting.model.common.UserInfo;
<<<<<<< Updated upstream

=======
import lombok.val;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
>>>>>>> Stashed changes
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoManagerImpl implements UserInfoManager {

    private final UserInfoDao userInfoDao;
    private final UserInfoP2CConverter userInfoP2CConverter;

    @Autowired
    public UserInfoManagerImpl(final UserInfoDao userInfoDao,
                               final UserInfoP2CConverter userInfoP2CConverter) {
        this.userInfoDao = userInfoDao;
        this.userInfoP2CConverter = userInfoP2CConverter;
    }

    @Override
    public UserInfo getUserInfoByUserId(Long userId) {
        return Optional.ofNullable(userInfoDao.getUserInfoById(userId))
                       .map(userInfoP2CConverter::convert)
                       .orElseThrow(() -> new ResourceNotFoundException(
                           String.format("User %s was not found", userId)));
    }

    @Override
    public void login(String username, String password) {
        // Get Subject
        val subject = SecurityUtils.getSubject();

        // Generate Token
        val token = new UsernamePasswordToken(username, password);

        subject.login(token);
    }

    @Override
    public void register(String username, String password) {
        userInfoDAO.createNewUser(username, password);
    }
}
