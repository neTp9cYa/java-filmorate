package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User create(@RequestBody final User user) {
        log.trace("Start to process of user creation request: {}", user);
        final User createdUser = userService.create(user);
        log.trace("End to process of user creation request: {}", user);
        return createdUser;
    }

    @PutMapping
    public User update(@RequestBody final User user) {
        log.trace("Start to process of user update request: {}", user);
        final User updatedUser = userService.update(user);
        log.trace("End to process of user update request: {}", user);
        return updatedUser;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }
}