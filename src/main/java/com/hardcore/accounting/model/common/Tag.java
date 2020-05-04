package com.hardcore.accounting.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    private Long id;
    private String description;
    private Long userId;
    private String status;
}
