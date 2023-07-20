package ru.yandex.practicum.filmorate.validator;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

class FilmValidatorTest {
    private final FilmValidator filmValidator = new FilmValidator();

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ",})
    void validateInvalidNameShouldThrow(final String name) {
        final Film film = correctFilm().name(name).build();
        final ValidationException exception = assertThrows(ValidationException.class, () -> filmValidator.validate(film));
        assertEquals("Name is not valid", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void validateNullEmptyDescriptionShouldNotThrow(final String description) {
        final Film film = correctFilm().description(description).build();
        assertDoesNotThrow(() -> filmValidator.validate(film));
    }

    @Test
    void validateLongDescriptionShouldNotThrow() {
        final Film film = correctFilm().description("*".repeat(200)).build();
        assertDoesNotThrow(() -> filmValidator.validate(film));
    }

    @Test
    void validateTooLongDescriptionShouldThrow() {
        final Film film = correctFilm().description("*".repeat(201)).build();
        final ValidationException exception = assertThrows(ValidationException.class, () -> filmValidator.validate(film));
        assertEquals("Description is not valid", exception.getMessage());
    }

    @Test
    void validateOldReleaseDateShouldNotThrow() {
        final Film film = correctFilm()
            .releaseDate(LocalDate.of(1895, 12, 28))
            .build();
        assertDoesNotThrow(() -> filmValidator.validate(film));
    }

    @Test
    void validateTooOldReleaseDateShouldThrow() {
        final Film film = correctFilm()
            .releaseDate(LocalDate.of(1895, 12, 28).minusDays(1))
            .build();
        final ValidationException exception = assertThrows(ValidationException.class, () -> filmValidator.validate(film));
        assertEquals("Release date is not valid", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void validateNotPositiveDurationShouldThrow(final int duration) {
        final Film user = correctFilm().duration(duration).build();
        final ValidationException exception = assertThrows(ValidationException.class, () -> filmValidator.validate(user));
        assertEquals("Duration is not valid", exception.getMessage());
    }

    private Film.FilmBuilder correctFilm() {
        return Film.builder()
            .id(1)
            .name("test")
            .description("test")
            .releaseDate(LocalDate.of(1895, 12, 28).plusDays(1))
            .duration(1);
    }

}