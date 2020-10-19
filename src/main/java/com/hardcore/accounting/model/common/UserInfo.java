package com.hardcore.accounting.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {
    private Long id;
    private String salt;
    private String username;
    private String password;

}
