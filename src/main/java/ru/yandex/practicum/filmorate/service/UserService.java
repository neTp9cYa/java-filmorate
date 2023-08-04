package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
        return userStorage.create(user);
    }

    public User update(final User user) {
        userValidator.validate(user);
        transform(user);
        userStorage.update(user);
        return user;
    }

    public User findOne(final int id) {
        final Optional<User> userOpt = userStorage.findOne(id);
        if (userOpt.isEmpty()) {
            throw new NotFoundException(String.format("User does not exists, id = %d", id));
        }
        return userOpt.get();
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public Collection<User> findFriends(final int id) {
        return userStorage.findFriends(id);
    }

    public Collection<User> findCommonFriends(final int id, final int otherId) {
        return userStorage.findCommonFriends(id, otherId);
    }

    public void addFriend(final int userId, final int friendId) {
        userStorage.addFriend(userId, friendId);
        userStorage.addFriend(friendId, userId);
    }

    public void removeFriend(final int userId, final int friendId) {
        userStorage.removeFriend(userId, friendId);
        userStorage.removeFriend(friendId, userId);
    }

    private void transform(final User user) {
        // имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
