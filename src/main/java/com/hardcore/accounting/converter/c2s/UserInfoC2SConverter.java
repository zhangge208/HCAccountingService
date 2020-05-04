package com.hardcore.accounting.converter.c2s;

import com.hardcore.accounting.model.common.UserInfo;

import com.google.common.base.Converter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserInfoC2SConverter extends Converter<UserInfo, com.hardcore.accounting.model.service.UserInfo> {

    @Override
    protected com.hardcore.accounting.model.service.UserInfo doForward(@NotNull UserInfo userInfo) {
        return com.hardcore.accounting.model.service.UserInfo.builder()
                                                             .id(userInfo.getId())
                                                             .username(userInfo.getUsername())
                                                             .build();
    }

    @Override
    protected UserInfo doBackward(@NotNull com.hardcore.accounting.model.service.UserInfo userInfo) {
        return UserInfo.builder()
                       .id(userInfo.getId())
                       .password(userInfo.getPassword())
                       .username(userInfo.getUsername())
                       .build();
    }
}
