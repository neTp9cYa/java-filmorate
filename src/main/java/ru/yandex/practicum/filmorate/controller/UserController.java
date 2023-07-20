package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.EntityExistsException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User create(@RequestBody final User user) {
        validate(user);
        transform(user);
        if (users.containsKey(user.getId())) {
            throw new EntityExistsException();
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody final User user) {
        validate(user);
        transform(user);
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    private void validate(final User user) {

        // электронная почта не может быть пустой и должна содержать символ @;
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException();
        }

        //логин не может быть пустым и содержать пробелы;
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException();
        }

        // дата рождения не может быть в будущем.
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException();
        }
    }

    private void transform(final User user) {

        // имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}