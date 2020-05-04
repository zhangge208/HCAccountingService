package com.hardcore.accounting.model.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Long id;
    private String username;
    private String password;
    private String salt;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
