package com.lab4.database_demo.service.exceptions.utils;

import lombok.Getter;

@Getter
public enum ErrorMessages {

    BRAND_NOT_FOUND("Brand with the given ID not found"),
    INVALID_BRAND_DATA("Invalid brand data provided"),
    DATABASE_ERROR("An unexpected database error occurred"),
    UNEXPECTED_ERROR("An unexpected error occurred while processing the request");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

}

