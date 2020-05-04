package com.hardcore.accounting.exception;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private BizErrorCode code;
    private ServiceException.ErrorType errorType;
    private String message;
    private int statusCode;
}
