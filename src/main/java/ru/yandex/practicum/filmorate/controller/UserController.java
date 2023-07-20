package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User create(@RequestBody final User user) {
        if (users.containsKey(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody final User user) {
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }
}