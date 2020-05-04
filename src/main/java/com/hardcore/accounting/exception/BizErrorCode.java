package com.hardcore.accounting.exception;

public enum BizErrorCode {
    INVALID_PARAMETER("INVALID_PARAMETER"),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND"),
    NO_AUTHORIZED("NO_AUTHORIZED"),
    INCORRECT_CREDENTIALS("INCORRECT_CREDENTIALS");

    private String message;
    BizErrorCode(String message) {
        this.message = message;
    }
}
