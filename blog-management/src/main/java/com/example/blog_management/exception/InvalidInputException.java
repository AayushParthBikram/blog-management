package com.example.blog_management.exception;

public class InvalidInputException  extends RuntimeException {
    public InvalidInputException (String message) {
        super(message);
    }
}
