package com.hardcore.accounting.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import com.hardcore.accounting.manager.UserInfoManager;
import com.hardcore.accounting.model.common.UserInfo;

import lombok.val;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserRealmTest {

    @Mock
    private UserInfoManager userInfoManager;

    @Mock
    private HashedCredentialsMatcher matcher;

    @InjectMocks
    private UserRealm userRealm;

    @Test
    void testDoGetAuthorizationInfo() {
        // Arrange
        val principal = new SimplePrincipalCollection();
        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> userRealm.doGetAuthorizationInfo(principal));
    }

    @Test
    void testDoGetAuthenticationInfo() {
        // Arrange
        val username = "hardcore";
        val password = "hardcore";
        val encodePassword = UUID.randomUUID().toString();
        val salt = UUID.randomUUID().toString();

        val token = new UsernamePasswordToken(username, password);

        val userInfo = UserInfo.builder()
                               .id(1L)
                               .username(username)
                               .password(encodePassword)
                               .salt(salt)
                               .build();

        doReturn(userInfo).when(userInfoManager).getUserInfoByUserName(username);
        // Act
        val result = userRealm.doGetAuthenticationInfo(token);
        // Assert
        assertThat(result).isNotNull();
    }
}
