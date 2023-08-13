package ru.yandex.practicum.filmorate.validator;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@Service
@Slf4j
public class UserValidator {
    public void validate(final User user) {

        // электронная почта не может быть пустой и должна содержать символ @;
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Email is not valid");
        }

        // логин не может быть пустым и содержать пробелы;
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Login is not valid");
        }

        // дата рождения не может быть в будущем.
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Birthday is not valid");
        }
    }
}
