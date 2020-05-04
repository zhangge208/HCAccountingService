package com.hardcore.accounting.converter.p2s;

import static org.assertj.core.api.Assertions.assertThat;

import com.hardcore.accounting.converter.p2c.UserInfoP2CConverter;
import com.hardcore.accounting.model.persistence.UserInfo;

import java.time.LocalDateTime;

import lombok.val;
import org.junit.jupiter.api.Test;

public class UserInfoP2CConverterTest {

    private UserInfoP2CConverter userInfoP2CConverter = new UserInfoP2CConverter();

    @Test
    void testDoForward() {
        // Arrange
        val userId = 100L;
        val username = "hardcore";
        val password = "hardcore";
        val createTime = LocalDateTime.now();

        val userInfoInPersistence = UserInfo.builder()
                                            .id(userId)
                                            .username(username)
                                            .password(password)
                                            .createTime(createTime)
                                            .build();
        // Act
        val result = userInfoP2CConverter.convert(userInfoInPersistence);

        // Assert
        assertThat(result).isNotNull()
                          .hasFieldOrPropertyWithValue("id", userId)
                          .hasFieldOrPropertyWithValue("username", username)
                          .hasFieldOrPropertyWithValue("password", password);
    }

    @Test
    void testDoBackward() {
        // Arrange
        val userId = 100L;
        val username = "hardcore";
        val password = "hardcore";

        val userInfoInCommon = com.hardcore.accounting.model.common.UserInfo.builder()
                                                                            .id(userId)
                                                                            .username(username)
                                                                            .password(password)
                                                                            .build();

        // Act
        val result = userInfoP2CConverter.reverse().convert(userInfoInCommon);
        // Assert
        assertThat(result).isNotNull()
                          .hasFieldOrPropertyWithValue("id", userId)
                          .hasFieldOrPropertyWithValue("username", username)
                          .hasFieldOrPropertyWithValue("createTime", null)
                          .hasFieldOrPropertyWithValue("password", password);

    }

}
