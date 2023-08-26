package com.example.BusinessProfileManagement.exception;

public class BusinessProfileValidationException extends RuntimeException{
  public BusinessProfileValidationException(String message) {
    super(message);
  }

  public BusinessProfileValidationException(String message, Exception e) {
    super(message,e);
  }
}
