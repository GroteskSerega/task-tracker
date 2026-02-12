package com.example.task_tracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidationException extends ResponseStatusException {
    public ValidationException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

  @Override
  public synchronized Throwable fillInStackTrace() {
    return this;
  }
}
