package com.hardcore.accounting.model.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Record {
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private String note;
    private String category;
    private List<Tag> tagList;
}
