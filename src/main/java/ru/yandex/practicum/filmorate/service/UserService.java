package ru.yandex.practicum.filmorate.service;

import java.util.List;
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
        userValidator.validateCreate(user);
        transform(user);
        return userStorage.create(user);
    }

    public User update(final User user) {
        userValidator.validateUpdate(user);

        userStorage.findUserById(user.getId())
            .orElseThrow(() -> new NotFoundException(String.format("User does not exists, id = %d", user.getId())));

        transform(user);
        userStorage.update(user);
        return user;
    }

    public User findOne(final int id) {
        return userStorage.findUserById(id)
            .orElseThrow(() -> new NotFoundException(String.format("User does not exists, id = %d", id)));
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public List<User> findFriends(final int id) {
        final User user = userStorage.findUserById(id)
            .orElseThrow(() -> new NotFoundException(String.format("User does not exists, id = %d", id)));

        return userStorage.findFriends(user.getId());
    }

    public List<User> findCommonFriends(final int id, final int otherId) {
        final User user = userStorage.findUserById(id)
            .orElseThrow(() -> new NotFoundException(String.format("User does not exists, id = %d", id)));

        final User otherUser = userStorage.findUserById(otherId)
            .orElseThrow(() -> new NotFoundException(String.format("User does not exists, id = %d", otherId)));

        return userStorage.findCommonFriends(user.getId(), otherUser.getId());
    }

    public void addFriend(final int userId, final int friendId) {
        final User user = userStorage.findUserById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User does not exists, id = %d", userId)));

        final User friend = userStorage.findUserById(friendId)
            .orElseThrow(() -> new NotFoundException(String.format("User does not exists, id = %d", friendId)));

        userStorage.addFriend(user.getId(), friend.getId());
    }

    public void removeFriend(final int userId, final int friendId) {
        final User user = userStorage.findUserById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User does not exists, id = %d", userId)));

        final User friend = userStorage.findUserById(friendId)
            .orElseThrow(() -> new NotFoundException(String.format("User does not exists, id = %d", friendId)));

        userStorage.removeFriend(user.getId(), friend.getId());
    }

    private void transform(final User user) {
        // имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
