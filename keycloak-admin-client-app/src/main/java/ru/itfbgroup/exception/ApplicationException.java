package ru.itfbgroup.exception;

public class ApplicationException extends RuntimeException{

    public ApplicationException(String errorMessage) {
        super(errorMessage);
    }

    public ApplicationException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
