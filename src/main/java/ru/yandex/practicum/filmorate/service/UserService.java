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

    public Collection<User> findMutualFriend(final int userId1, final int userId2) {
        final User user1 = userStorage.findOne(userId1);
        if (user1 == null) {
            log.warn("Try to get mutual friends for non existed user, user id = {}", userId1);
            throw new UpdateException(String.format("User does not exists, id = %d", userId1));
        }

        final User user2 = userStorage.findOne(userId2);
        if (user2 == null) {
            log.warn("Try to get mutual friends for non existed user, user id = {}", userId2);
            throw new UpdateException(String.format("User does not exists, id = %d", userId2));
        }

        return user1.getFriends().stream()
            .filter(user2.getFriends()::contains)
            .map(userStorage::findOne)
            .collect(Collectors.toList());
    }

    public void addFriend(final int whoId, final int whomId) {
        final User who = userStorage.findOne(whoId);
        if (who == null) {
            log.warn("Try to add friend on behalf of non existed user, user id = {}", whoId);
            throw new UpdateException(String.format("User does not exists, id = %d", whoId));
        }

        final User whom = userStorage.findOne(whomId);
        if (whom == null) {
            log.warn("Try to add non existed user as friend, user id = {}", whomId);
            throw new UpdateException(String.format("User does not exists, id = %d", whomId));
        }

        who.getFriends().add(whom.getId());
        whom.getFriends().add(who.getId());

        userStorage.update(who);
        userStorage.update(whom);
    }

    public void removeFriend(final int whoId, final int whomId) {
        final User who = userStorage.findOne(whoId);
        if (who == null) {
            log.warn("Try to remove friend on behalf of non existed user, user id = {}", whoId);
            throw new UpdateException(String.format("User does not exists, id = %d", whoId));
        }

        final User whom = userStorage.findOne(whomId);
        if (whom == null) {
            log.warn("Try to remove non existed user from friends, user id = {}", whomId);
            throw new UpdateException(String.format("User does not exists, id = %d", whomId));
        }

        who.getFriends().remove(whom.getId());
        whom.getFriends().remove(who.getId());

        userStorage.update(who);
        userStorage.update(whom);
    }

    private void transform(final User user) {
        // имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
