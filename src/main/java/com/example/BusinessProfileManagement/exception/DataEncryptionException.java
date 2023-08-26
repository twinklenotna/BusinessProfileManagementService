package com.example.BusinessProfileManagement.exception;

public class DataEncryptionException extends RuntimeException{
  public DataEncryptionException(String message, Exception e) {
    super(message,e);
  }
}
