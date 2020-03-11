package com.hardcore.accounting.exception;


import lombok.val;
import org.springframework.http.HttpStatus;
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
                             .body(errorResponse);
    }




}
