package com.hardcore.accounting.dao;

import com.hardcore.accounting.dao.mapper.UserInfoMapper;
import com.hardcore.accounting.model.persistence.UserInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserInfoDaoImpl implements UserInfoDao {

    private final UserInfoMapper userInfoMapper;

    @Override
    public UserInfo getUserInfoById(Long id) {
        return userInfoMapper.getUserInfoByUserId(id);
    }

    @Override
    public void createNewUser(UserInfo userInfo) {
        val row = userInfoMapper.createNewUser(userInfo);
        log.debug("Result: {}, use information: {}", row, userInfo);
    }

    @Override
    public UserInfo getUserInfoByUserName(String username) {
        return userInfoMapper.getUserInfoByUserName(username);
    }
}
