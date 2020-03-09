package com.hardcore.accounting.converter.c2s;

import com.google.common.base.Converter;
import com.hardcore.accounting.model.common.UserInfo;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class UserInfoC2SConverter extends Converter<UserInfo, com.hardcore.accounting.model.service.UserInfo> {

    @Override
    protected com.hardcore.accounting.model.service.UserInfo doForward(UserInfo userInfo) {
        return com.hardcore.accounting.model.service.UserInfo.builder()
                                                             .id(userInfo.getId())
                                                             .username(userInfo.getUsername())
                                                             .password(userInfo.getPassword())
                                                             .build();
    }

    @Override
    protected UserInfo doBackward(com.hardcore.accounting.model.service.UserInfo userInfo) {
        return UserInfo.builder()
                       .id(userInfo.getId())
                       .password(userInfo.getPassword())
                       .username(userInfo.getUsername())
                       .build();
    }
}
