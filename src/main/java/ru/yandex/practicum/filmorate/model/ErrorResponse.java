package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String error;
    private final String stackTrace;

    public ErrorResponse(final String error) {
        this(error, null);
    }

    public ErrorResponse(final String error, final String stackTrace) {
        this.error = error;
        this.stackTrace = stackTrace;
    }
}