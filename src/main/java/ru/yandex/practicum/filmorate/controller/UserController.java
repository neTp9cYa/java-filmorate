package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.EntityExistsException;
import ru.yandex.practicum.filmorate.exception.EntityNotExistsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private final UserValidator userValidator = new UserValidator();

    @PostMapping
    public User create(@RequestBody final User user) {
        userValidator.validate(user);
        transform(user);
        if (users.containsKey(user.getId())) {
            log.warn("Try to create user with existed id: {}", user);
            throw new EntityExistsException();
        }
        users.put(user.getId(), user);
        log.warn("User was created: {}", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody final User user) {
        userValidator.validate(user);
        transform(user);
        if (!users.containsKey(user.getId())) {
            log.warn("Try to update not existed user: {}", user);
            throw new EntityNotExistsException();
        }
        users.put(user.getId(), user);
        log.warn("User was updated: {}", user);
        return user;
    }

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    private void transform(final User user) {
        // имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}