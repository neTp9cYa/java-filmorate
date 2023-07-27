package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserValidator userValidator;
    private final UserStorage userStorage;

    public User create(final User user) {
        userValidator.validate(user);
        transform(user);
        final User createdUser = userStorage.create(user);
        return createdUser;
    }

    public User update(final User user) {
        userValidator.validate(user);
        transform(user);
        final User updatedUser = userStorage.update(user);
        return updatedUser;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    private void transform(final User user) {
        // имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
