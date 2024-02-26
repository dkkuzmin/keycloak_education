package ru.itfbgroup.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

    private final String parameterName;
    private final String parameterType;

    public ValidationException(String parameterName, String parameterType, String message) {
        super(message);
        this.parameterName = parameterName;
        this.parameterType = parameterType;
    }
}
