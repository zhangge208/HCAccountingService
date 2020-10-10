package com.hardcore.accounting.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * HC Accounting Service Exception.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceException extends RuntimeException {
    private int statusCode;
    private BizErrorCode errorCode; // biz error code
    private ServiceException.ErrorType errorType; // Service, Client, Unknown

    public enum ErrorType {
        Client,
        Service,
        Unknown
    }

    public ServiceException(String message) {
        super(message);
    }


}
