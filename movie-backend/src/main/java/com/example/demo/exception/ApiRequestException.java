package com.example.demo.exception;

public class ApiRequestException extends RuntimeException {
  private final ErrorCode errorCode;

  public ApiRequestException( String message) {
    super(message);
    this.errorCode = ErrorCode.INVALID_REQUEST_BODY;
  }

  public ApiRequestException( String message , ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }
  public ApiRequestException( String message , Throwable cause) {
    super(message, cause);
    this.errorCode = ErrorCode.INVALID_REQUEST_BODY;

  }

}
