package com.hardcore.accounting.converter.p2c;

import com.hardcore.accounting.model.persistence.UserInfo;

import com.google.common.base.Converter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserInfoP2CConverter extends Converter<UserInfo, com.hardcore.accounting.model.common.UserInfo> {

    @Override
    protected com.hardcore.accounting.model.common.UserInfo doForward(@NotNull UserInfo userInfo) {
        return com.hardcore.accounting.model.common.UserInfo.builder()
                                                            .id(userInfo.getId())
                                                            .username(userInfo.getUsername())
                                                            .password(userInfo.getPassword())
                                                            .salt(userInfo.getSalt())
                                                            .build();
    }

    @Override
    protected UserInfo doBackward(@NotNull com.hardcore.accounting.model.common.UserInfo userInfo) {
        return UserInfo.builder()
                       .id(userInfo.getId())
                       .username(userInfo.getUsername())
                       .password(userInfo.getPassword())
                       .build();
    }
}
