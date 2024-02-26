package ru.itfbgroup.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.itfbgroup.exception.NotFoundException;
import ru.itfbgroup.exception.ValidationException;
import ru.itfbgroup.model.error.ErrorResponse;
import ru.itfbgroup.model.error.ValidationErrorResponse;

import java.util.List;

@Slf4j
//@RestControllerAdvice
public class CommonExceptionHandler {

    private static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    private static final String VALIDATION_MESSAGE = "Некорректные данные";
    private static final String PARAM_TYPE = "requestParam";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorResponse handleException(Exception e) {
        logError(e);
        return new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "Внутренняя ошибка сервиса"
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    ValidationErrorResponse handleBadRequest(MissingServletRequestParameterException e) {
        logError(e);
        return createValidationErrorResponse(
                e.getParameterName(),
                PARAM_TYPE,
                "Должен быть заполнен ID объекта"
        );
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    ErrorResponse handleNotSupportedMediaType(HttpMediaTypeNotSupportedException e) {
        logError(e);
        return new ErrorResponse(
                "CLIENT_ERROR",
                "Unsupported Media Type"
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    ValidationErrorResponse handleBadRequest(ValidationException e) {
        logError(e);
        return createValidationErrorResponse(e.getParameterName(), e.getParameterType(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ValidationErrorResponse handleBadRequest(MethodArgumentTypeMismatchException e) {
        logError(e);
        return createValidationErrorResponse(e.getName(), PARAM_TYPE, "Некорректный формат ID объекта");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    ErrorResponse handleNotFound(NotFoundException e) {
        logError(e);
        return new ErrorResponse("NOT_FOUND", e.getMessage());
    }

    private ValidationErrorResponse createValidationErrorResponse(String parameterName,
                                                                  String parameterType,
                                                                  String message) {
        ValidationErrorResponse.Violation violation = new ValidationErrorResponse.Violation(
                parameterName,
                parameterType,
                message
        );
        ValidationErrorResponse.ErrorDetails errorDetails =
                new ValidationErrorResponse.ErrorDetails(List.of(violation));
        return new ValidationErrorResponse(VALIDATION_ERROR, VALIDATION_MESSAGE, errorDetails);
    }

    private void logError(Exception e) {
        log.error("Error occurred: ", e);
    }
}
