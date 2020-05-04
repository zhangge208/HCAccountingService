package com.hardcore.accounting.model.common;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class Record {
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private String note;
    private String category;
    private String status;
    private List<Tag> tagList;
}
