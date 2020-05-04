package com.hardcore.accounting.exception;

import lombok.val;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //DRY : Don't repeat yourself
    @ExceptionHandler(ServiceException.class)
    ResponseEntity<?> handleServiceException(ServiceException ex) {
        val errorResponse = ErrorResponse.builder()
                                         .statusCode(ex.getStatusCode())
                                         .message(ex.getMessage())
                                         .code(ex.getErrorCode())
                                         .errorType(ex.getErrorType())
                                         .build();

        return ResponseEntity.status(ex.getStatusCode() != 0 ? ex.getStatusCode()
                                                             : HttpStatus.INTERNAL_SERVER_ERROR.value())
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(errorResponse);
    }

    @ExceptionHandler(IncorrectCredentialsException.class)
    ResponseEntity<?> handleIncorrectCredentialsException(IncorrectCredentialsException ex) {
        val errorResponse = ErrorResponse.builder()
                                         .statusCode(HttpStatus.BAD_REQUEST.value())
                                         .message(ex.getMessage())
                                         .code(BizErrorCode.INCORRECT_CREDENTIALS)
                                         .errorType(ServiceException.ErrorType.Client)
                                         .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(errorResponse);
    }


}
