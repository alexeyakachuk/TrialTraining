package com.example.trial_training.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handNotFoundException(final NotFoundException e) {
        log.error("Произошла ошибка таких данных нет");
        return Map.of("Произошла ошибка", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handConflictException(final UserConflictDataException e) {
        log.error("Ошибка пользователь с такими данными уже есть");
        return Map.of("Произошла ошибка", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handValidationException(final ValidationException e) {
        log.error("Произошла ошибка не корректно веденны данные");
        return Map.of("Произощла ошибка", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleUnexpectedException(final RuntimeException e) {
        log.error("Неожиданная ошибка", e);
        return Map.of("error", "Внутренняя ошибка сервера");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handConflictTimeException(final ConflictTimeException e) {
        log.error("Произошла ошибка не корретно веденно время тренировки");
        return Map.of("Произошла ошибка", e.getMessage());
    }
}
