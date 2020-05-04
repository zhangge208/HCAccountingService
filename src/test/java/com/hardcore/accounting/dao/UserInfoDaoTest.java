package com.hardcore.accounting.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.hardcore.accounting.dao.mapper.UserInfoMapper;
import com.hardcore.accounting.model.persistence.UserInfo;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class UserInfoDaoTest {

    @Mock
    private UserInfoMapper userInfoMapper;

    @InjectMocks
    private UserInfoDaoImpl userInfoDAO;

    @Test
    void testGetUserInfoById() {
        // Arrange
        val userId = 100L;
        val username = "hardcore";
        val password = "hardcore";
        val createTime = LocalDateTime.now();

        val userInfo = UserInfo.builder()
                               .id(userId)
                               .username(username)
                               .password(password)
                               .createTime(createTime)
                               .build();
        doReturn(userInfo).when(userInfoMapper).getUserInfoByUserId(userId);
        // Act
        val result = userInfoDAO.getUserInfoById(userId);
        // Assert
        assertEquals(userInfo, result);

        verify(userInfoMapper).getUserInfoByUserId(userId);
    }

    @Test
    void testCreateUsername() {
        // Arrange
        val userId = 100L;
        val username = "hardcore";
        val password = "hardcore";
        val createTime = LocalDateTime.now();

        val userInfo = UserInfo.builder()
                               .id(userId)
                               .username(username)
                               .password(password)
                               .createTime(createTime)
                               .build();
        doReturn(1).when(userInfoMapper).createNewUser(userInfo);
        // Act
        userInfoDAO.createNewUser(userInfo);
        // Assert
        //assertEquals(userInfo, result);
        verify(userInfoMapper).createNewUser(any(UserInfo.class));
    }

    @Test
    void testGetUserInfoByUsername() {
        // Arrange
        val userId = 100L;
        val username = "hardcore";
        val password = "hardcore";
        val createTime = LocalDateTime.now();

        val userInfo = UserInfo.builder()
                               .id(userId)
                               .username(username)
                               .password(password)
                               .createTime(createTime)
                               .build();
        doReturn(userInfo).when(userInfoMapper).getUserInfoByUserName(username);
        // Act
        val result = userInfoDAO.getUserInfoByUserName(username);
        // Assert
        assertEquals(userInfo, result);
        verify(userInfoMapper).getUserInfoByUserName(username);

    }
}
