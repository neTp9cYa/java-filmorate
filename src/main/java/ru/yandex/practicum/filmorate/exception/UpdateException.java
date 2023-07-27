package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not Valid")
public class UpdateException extends RuntimeException {
    public UpdateException(final String message) {
        super(message);
    }
}
