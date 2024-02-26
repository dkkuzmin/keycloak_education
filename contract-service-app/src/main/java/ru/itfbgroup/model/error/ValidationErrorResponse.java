package ru.itfbgroup.model.error;

import lombok.Getter;
import lombok.Value;

import java.util.List;

@Getter
public class ValidationErrorResponse extends ErrorResponse {
    private final ErrorDetails details;

    public ValidationErrorResponse(String status, String message, ErrorDetails details) {
        super(status, message);
        this.details = details;
    }

    @Value
    public static class ErrorDetails {
        List<Violation> violations;
    }

    @Value
    public static class Violation {
        String name;
        String type;
        String message;
    }
}
