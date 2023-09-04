package ru.yandex.practicum.filmorate.validator;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

@Service
@Slf4j
public class FilmValidator {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final int MAX_DESCRIPTION_LENGTH = 200;

    public void validateUpdate(final Film film) {
        if (film.getId() == null || film.getId() <= 0) {
            throw new ValidationException("Id is not valid");
        }
        validateCreate(film);
    }

    public void validateCreate(final Film film) {

        // название не может быть пустым
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Name is not valid");
        }

        // максимальная длина описания должна быть ограничена
        if (film.getDescription() != null && film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException("Description is not valid");
        }

        // минимальная дата релиза должна быть ограничена;
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Release date is not valid");
        }

        // продолжительность фильма должна быть положительной.
        if (film.getDuration() <= 0) {
            throw new ValidationException("Duration is not valid");
        }
    }
}
