package com.hardcore.accounting.converter.c2s;

import static org.assertj.core.api.Assertions.assertThat;
import com.hardcore.accounting.converter.c2s.UserInfoC2SConverter;
import com.hardcore.accounting.model.common.UserInfo;
import lombok.val;
import org.junit.jupiter.api.Test;

public class UserInfoC2SConverterTest {

    private UserInfoC2SConverter userInfoC2SConverter = new UserInfoC2SConverter();

    @Test
    void testDoForward() {
        // Arrange
        val userId = 100L;
        val username = "hardcore";
        val password = "hardcore";

        val userInfoInCommon = UserInfo.builder()
                                       .id(userId)
                                       .username(username)
                                       .password(password)
                                       .build();
        // Act
        val result = userInfoC2SConverter.convert(userInfoInCommon);

        // Assert
        assertThat(result).isNotNull()
                          .hasFieldOrPropertyWithValue("id", userId)
                          .hasFieldOrPropertyWithValue("username", username)
                          .hasFieldOrPropertyWithValue("password", null);
    }

    @Test
    void testDoBackward() {
        // Arrange
        val userId = 100L;
        val username = "hardcore";
        val password = "hardcore";

        val userInfoInCommon = com.hardcore.accounting.model.service.UserInfo.builder()
                                                                             .id(userId)
                                                                             .username(username)
                                                                             .password(password)
                                                                             .build();

        // Act
        val result = userInfoC2SConverter.reverse().convert(userInfoInCommon);
        // Assert
        assertThat(result).isNotNull()
                          .hasFieldOrPropertyWithValue("id", userId)
                          .hasFieldOrPropertyWithValue("username", username)
                          .hasFieldOrPropertyWithValue("password", password);

    }
}
