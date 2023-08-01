package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UpdateException;
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

    public User findOne(final int id) {
        return userStorage.findOne(id);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public Collection<User> findFriends(final int id) {
        final User user = userStorage.findOne(id);
        if (user == null) {
            log.warn("Try to get friends for non existed user, user id = {}", id);
            throw new UpdateException(String.format("User does not exists, id = %d", id));
        }

        return user.getFriends()
            .stream()
            .map(userStorage::findOne)
            .collect(Collectors.toList());
    }

    public Collection<User> findCommonFriends(final int id, final int otherId) {
        final User user = userStorage.findOne(id);
        if (user == null) {
            log.warn("Try to get mutual friends for non existed user, user id = {}", id);
            throw new UpdateException(String.format("User does not exists, id = %d", id));
        }

        final User otherUser = userStorage.findOne(otherId);
        if (otherUser == null) {
            log.warn("Try to get mutual friends for non existed user, user id = {}", otherId);
            throw new UpdateException(String.format("User does not exists, id = %d", otherId));
        }

        return user.getFriends().stream()
            .filter(otherUser.getFriends()::contains)
            .map(userStorage::findOne)
            .collect(Collectors.toList());
    }

    public void addFriend(final int userId, final int friendId) {
        final User user = userStorage.findOne(userId);
        if (user == null) {
            log.warn("Try to add friend on behalf of non existed user, user id = {}", userId);
            throw new UpdateException(String.format("User does not exists, id = %d", userId));
        }

        final User friend = userStorage.findOne(friendId);
        if (friend == null) {
            log.warn("Try to add non existed user as friend, user id = {}", friendId);
            throw new UpdateException(String.format("User does not exists, id = %d", friendId));
        }

        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());

        userStorage.update(user);
        userStorage.update(friend);
    }

    public void removeFriend(final int userId, final int friendId) {
        final User user = userStorage.findOne(userId);
        if (user == null) {
            log.warn("Try to remove friend on behalf of non existed user, user id = {}", userId);
            throw new UpdateException(String.format("User does not exists, id = %d", userId));
        }

        final User friend = userStorage.findOne(friendId);
        if (friend == null) {
            log.warn("Try to remove non existed user from friends, user id = {}", friendId);
            throw new UpdateException(String.format("User does not exists, id = %d", friendId));
        }

        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());

        userStorage.update(user);
        userStorage.update(friend);
    }

    private void transform(final User user) {
        // имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
