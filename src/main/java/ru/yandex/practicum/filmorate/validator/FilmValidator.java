package ru.yandex.practicum.filmorate.validator;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

@Slf4j
public class FilmValidator {
    public void validate(final Film film) {

        // название не может быть пустым
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Film name is not valid: {}", film);
            throw new ValidationException("Name is not valid");
        }

        // максимальная длина описания — 200 символов
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.warn("Film description is not valid: {}", film);
            throw new ValidationException("Description is not valid");
        }

        // дата релиза — не раньше 28 декабря 1895 года;
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Film release date  is not valid: {}", film);
            throw new ValidationException("Release date is not valid");
        }

        // продолжительность фильма должна быть положительной.
        if (film.getDuration() <= 0) {
            log.warn("Film duration is not valid: {}", film);
            throw new ValidationException("Duration is not valid");
        }
    }
}
