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
import ru.yandex.practicum.filmorate.model.User;

class UserValidatorTest {
    private final UserValidator userValidator = new UserValidator();

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "without.at"})
    void validateInvalidEmailShouldThrow(final String email) {
        final User user = correctUser().email(email).build();
        final ValidationException exception =
            assertThrows(ValidationException.class, () -> userValidator.validate(user));
        assertEquals("Email is not valid", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "contains space"})
    void validateInvalidLoginShouldThrow(final String login) {
        final User user = correctUser().login(login).build();
        final ValidationException exception =
            assertThrows(ValidationException.class, () -> userValidator.validate(user));
        assertEquals("Login is not valid", exception.getMessage());
    }

    @Test
    void validateNullBirthdayShouldNotThrow() {
        final User user = correctUser().birthday(null).build();
        assertDoesNotThrow(() -> userValidator.validate(user));
    }

    @Test
    void validaBirthdayInFeatureShouldThrow() {
        final User user = correctUser().birthday(LocalDate.now().plusDays(1)).build();
        final ValidationException exception =
            assertThrows(ValidationException.class, () -> userValidator.validate(user));
        assertEquals("Birthday is not valid", exception.getMessage());
    }

    @Test
    void validateTodayBirthdayShouldNotThrow() {
        final User user = correctUser().birthday(LocalDate.now()).build();
        assertDoesNotThrow(() -> userValidator.validate(user));
    }

    @Test
    void validateValidUserShouldNotThrow() {
        final User user = correctUser().build();
        assertDoesNotThrow(() -> userValidator.validate(user));
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