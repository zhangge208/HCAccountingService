package com.hardcore.accounting.manager;

import com.hardcore.accounting.converter.p2c.UserInfoP2CConverter;
import com.hardcore.accounting.dao.UserInfoDao;
import com.hardcore.accounting.exception.InvalidParameterException;
import com.hardcore.accounting.exception.ResourceNotFoundException;
import com.hardcore.accounting.model.common.UserInfo;

import lombok.val;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserInfoManagerImpl implements UserInfoManager {
    public static final int HASH_ITERATIONS = 1000;
    private final UserInfoDao userInfoDao;
    private final UserInfoP2CConverter userInfoP2CConverter;

    @Autowired
    public UserInfoManagerImpl(final UserInfoDao userInfoDao,
                               final UserInfoP2CConverter userInfoP2CConverter) {
        this.userInfoDao = userInfoDao;
        this.userInfoP2CConverter = userInfoP2CConverter;
    }

    @Override
    @Cacheable(value = "userinfo", key = "#userId")
    public UserInfo getUserInfoByUserId(Long userId) {
        return Optional.ofNullable(userInfoDao.getUserInfoById(userId))
                       .map(userInfoP2CConverter::convert)
                       .orElseThrow(() -> new ResourceNotFoundException(
                           String.format("User %s was not found", userId)));
    }

    @Override
    @Cacheable(value = "userinfo_by_name", key = "#username")
    public UserInfo getUserInfoByUserName(String username) {
        val userInfo = Optional.ofNullable(userInfoDao.getUserInfoByUserName(username))
                               .orElseThrow(() -> new ResourceNotFoundException(
                                   String.format("User name %s was not found", username)));
        return userInfoP2CConverter.convert(userInfo);
    }

    @Override
    public void login(String username, String password) {
        // Get subject
        val subject = SecurityUtils.getSubject();
        // Construct token
        val usernamePasswordToken = new UsernamePasswordToken(username, password);

        //login
        subject.login(usernamePasswordToken);

    }

    @Override
    @CachePut(value = "userinfo", key = "#result.id")
    public UserInfo register(String username, String password) {
        val userInfo = userInfoDao.getUserInfoByUserName(username);
        if (userInfo != null) {
            throw new InvalidParameterException(String.format("The user %s was registered.", username));
        }

        // Set random salt
        String salt = UUID.randomUUID().toString();
        String encryptedPassword = new Sha256Hash(password, salt, HASH_ITERATIONS).toBase64();

        val newUserInfo = com.hardcore.accounting.model.persistence.UserInfo.builder()
                                                                           .username(username)
                                                                           .password(encryptedPassword)
                                                                           .salt(salt)
                                                                           .createTime(LocalDateTime.now())
                                                                           .build();
        userInfoDao.createNewUser(newUserInfo);
        return userInfoP2CConverter.convert(newUserInfo);
    }
}
