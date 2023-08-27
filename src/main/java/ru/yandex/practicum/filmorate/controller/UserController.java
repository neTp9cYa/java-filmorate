package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        log.info("Start to process of user creation request: {}", user);
        final User createdUser = userService.create(user);
        log.info("End to process of user creation request: {}", user);
        return createdUser;
    }

    @PutMapping
    public User update(@RequestBody final User user) {
        log.info("Start to process of user update request: {}", user);
        final User updatedUser = userService.update(user);
        log.info("End to process of user update request: {}", user);
        return updatedUser;
    }

    @GetMapping("/{id}")
    public User findOne(@PathVariable final int id) {
        return userService.findOne(id);
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findFriends(@PathVariable final int id) {
        return userService.findFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findCommonFriends(@PathVariable final int id,
                                              @PathVariable final int otherId) {
        return userService.findCommonFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable final int id,
                          @PathVariable final int friendId) {
        log.info("Start to process of add friend request: user {} and friend {}", id, friendId);
        userService.addFriend(id, friendId);
        log.info("End to process of add friend request: user {} and friend {}", id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable final int id,
                             @PathVariable final int friendId) {
        log.info("Start to process of remove friend request: user {} and friend {}", id, friendId);
        userService.removeFriend(id, friendId);
        log.info("End to process of remove friend request: user {} and friend {}", id, friendId);
    }
}