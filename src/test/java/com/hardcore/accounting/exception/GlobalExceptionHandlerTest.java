package com.hardcore.accounting.exception;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.val;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class GlobalExceptionHandlerTest {
    public static final int STATUS_CODE = 400;

    @Test
    public void testHandleServiceException() {
        // Arrange
        val exception = new ResourceNotFoundException("The resource not found exception.");

        val globalExceptionHandler = new GlobalExceptionHandler();

        val errorResponse = ErrorResponse.builder()
                                         .statusCode(exception.getStatusCode())
                                         .message(exception.getMessage())
                                         .code(exception.getErrorCode())
                                         .errorType(exception.getErrorType())
                                         .build();
        // Act
        val result = globalExceptionHandler.handleServiceException(exception);

        // Assert
        assertThat(result).isNotNull()
                          .hasFieldOrPropertyWithValue("status", exception.getStatusCode())
                          .hasFieldOrPropertyWithValue("body", errorResponse);

    }

    @Test
    public void testHandleServiceExceptionWithoutStatusCode() {
        // Arrange
        val exception = new ServiceException("The service exception.");
        val globalExceptionHandler = new GlobalExceptionHandler();

        val errorResponse = ErrorResponse.builder()
                                         .statusCode(exception.getStatusCode())
                                         .message(exception.getMessage())
                                         .code(exception.getErrorCode())
                                         .errorType(exception.getErrorType())
                                         .build();
        // Act
        val result = globalExceptionHandler.handleServiceException(exception);

        // Assert
        assertThat(result).isNotNull()
                          .hasFieldOrPropertyWithValue("status", HttpStatus.INTERNAL_SERVER_ERROR.value())
                          .hasFieldOrPropertyWithValue("body", errorResponse);
    }

    @Test
    public void testHandleIncorrectCredentialsException() {
        // Arrange
        val exception = new IncorrectCredentialsException("The credentials are invalid.");

        val globalExceptionHandler = new GlobalExceptionHandler();

        val errorResponse = ErrorResponse.builder()
                                         .statusCode(HttpStatus.BAD_REQUEST.value())
                                         .message(exception.getMessage())
                                         .code(BizErrorCode.INCORRECT_CREDENTIALS)
                                         .errorType(ServiceException.ErrorType.Client)
                                         .build();
        // Act
        val result = globalExceptionHandler.handleIncorrectCredentialsException(exception);

        // Assert
        assertThat(result).isNotNull()
                          .hasFieldOrPropertyWithValue("status", STATUS_CODE)
                          .hasFieldOrPropertyWithValue("body", errorResponse);
    }
}
