package com.lab4.database_demo.service.exceptions;

public class BadRequestException extends Exception{

    public BadRequestException() {
        super("Error in your request. Try again");
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }
}
