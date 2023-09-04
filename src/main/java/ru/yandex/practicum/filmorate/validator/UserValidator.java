package ru.yandex.practicum.filmorate.validator;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@Service
@Slf4j
public class UserValidator {
    public void validateUpdate(final User user) {
        if (user.getId() == null || user.getId() <= 0) {
            throw new ValidationException("Id is not valid");
        }
        validateCreate(user);
    }

    public void validateCreate(final User user) {

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
