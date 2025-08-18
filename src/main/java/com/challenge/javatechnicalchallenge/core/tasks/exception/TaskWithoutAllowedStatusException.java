package com.challenge.javatechnicalchallenge.core.tasks.exception;

public class TaskWithoutAllowedStatusException extends RuntimeException {
    public TaskWithoutAllowedStatusException(String message) {
        super(message);
    }

    public TaskWithoutAllowedStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
