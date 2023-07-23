package ru.yandex.practicum.filmorate.validator;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

@DisplayName("FilmValidator")
class FilmValidatorTest {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private final FilmValidator filmValidator = new FilmValidator();

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ",})
    @DisplayName("Невалидный name приводит к исключению")
    void validateInvalidNameShouldThrow(final String name) {
        final Film film = correctFilm().name(name).build();
        final ValidationException exception =
            assertThrows(ValidationException.class, () -> filmValidator.validate(film));
        assertEquals("Name is not valid", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    @DisplayName("Отсутсвие или пустое description допусимо")
    void validateNullEmptyDescriptionShouldNotThrow(final String description) {
        final Film film = correctFilm().description(description).build();
        assertDoesNotThrow(() -> filmValidator.validate(film));
    }

    @Test
    @DisplayName("Длинное description допусимо")
    void validateLongDescriptionShouldNotThrow() {
        final Film film = correctFilm().description("*".repeat(MAX_DESCRIPTION_LENGTH)).build();
        assertDoesNotThrow(() -> filmValidator.validate(film));
    }

    @Test
    @DisplayName("Длинный login приводит к исключению")
    void validateTooLongDescriptionShouldThrow() {
        final Film film = correctFilm().description("*".repeat(201)).build();
        final ValidationException exception =
            assertThrows(ValidationException.class, () -> filmValidator.validate(film));
        assertEquals("Description is not valid", exception.getMessage());
    }

    @Test
    @DisplayName("Старое releaseDate допусимо")
    void validateOldReleaseDateShouldNotThrow() {
        final Film film = correctFilm()
            .releaseDate(MIN_RELEASE_DATE)
            .build();
        assertDoesNotThrow(() -> filmValidator.validate(film));
    }

    @Test
    @DisplayName("Очень старое releaseDate приводит к исключению")
    void validateTooOldReleaseDateShouldThrow() {
        final Film film = correctFilm()
            .releaseDate(MIN_RELEASE_DATE.minusDays(1))
            .build();
        final ValidationException exception =
            assertThrows(ValidationException.class, () -> filmValidator.validate(film));
        assertEquals("Release date is not valid", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("Нулевое или отрицательно duration приводит к исключению")
    void validateNotPositiveDurationShouldThrow(final int duration) {
        final Film user = correctFilm().duration(duration).build();
        final ValidationException exception =
            assertThrows(ValidationException.class, () -> filmValidator.validate(user));
        assertEquals("Duration is not valid", exception.getMessage());
    }

    @Test
    @DisplayName("Корректные данные проходят валидацию")
    void validateValidUserShouldNotThrow() {
        final Film film = correctFilm().build();
        assertDoesNotThrow(() -> filmValidator.validate(film));
    }

    private Film.FilmBuilder correctFilm() {
        return Film.builder()
            .id(1)
            .name("test")
            .description("test")
            .releaseDate(MIN_RELEASE_DATE.plusDays(1))
            .duration(1);
    }

}