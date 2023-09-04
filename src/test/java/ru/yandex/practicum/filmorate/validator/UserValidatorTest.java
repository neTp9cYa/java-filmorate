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
import ru.yandex.practicum.filmorate.model.User;

@DisplayName("UserValidator")
class UserValidatorTest {
    private final UserValidator userValidator = new UserValidator();

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "without.at"})
    @DisplayName("Невалидный email приводит к исключению")
    void validateInvalidEmailShouldThrow(final String email) {
        final User user = correctUser().email(email).build();
        final ValidationException exception =
            assertThrows(ValidationException.class, () -> userValidator.validate(user),
                "Exception does not thrown");
        assertEquals("Email is not valid", exception.getMessage(), "Execption message is not valid");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "contains space"})
    @DisplayName("Невалидный login приводит к исключению")
    void validateInvalidLoginShouldThrow(final String login) {
        final User user = correctUser().login(login).build();
        final ValidationException exception =
            assertThrows(ValidationException.class, () -> userValidator.validate(user),
                "Exception is not thrown");
        assertEquals("Login is not valid", exception.getMessage(), "Exception is thrown");
    }

    @Test
    @DisplayName("Поле Birthday может быть не задано")
    void validateNullBirthdayShouldNotThrow() {
        final User user = correctUser().birthday(null).build();
        assertDoesNotThrow(() -> userValidator.validate(user), "Exception is thrown");
    }

    @Test
    @DisplayName("Birthday в будущем приводит к исключению")
    void validaBirthdayInFeatureShouldThrow() {
        final User user = correctUser().birthday(LocalDate.now().plusDays(1)).build();
        final ValidationException exception =
            assertThrows(ValidationException.class, () -> userValidator.validate(user),
                "Exception is not thrown");
        assertEquals("Birthday is not valid", exception.getMessage(), "Execption message is not valid");
    }

    @Test
    @DisplayName("Поле Birthday может быть равно сегодня")
    void validateTodayBirthdayShouldNotThrow() {
        final User user = correctUser().birthday(LocalDate.now()).build();
        assertDoesNotThrow(() -> userValidator.validate(user), "Exception is thrown");
    }

    @Test
    @DisplayName("Корректные данные проходят валидацию")
    void validateValidUserShouldNotThrow() {
        final User user = correctUser().build();
        assertDoesNotThrow(() -> userValidator.validate(user), "Exception is thrown");
    }

    private User.UserBuilder correctUser() {
        return User.builder()
            .id(1)
            .email("test@test.test")
            .login("test")
            .name("test")
            .birthday(LocalDate.now().minusYears(18));
    }

}