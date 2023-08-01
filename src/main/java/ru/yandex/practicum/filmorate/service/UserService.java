package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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

        final Optional<User> storedUserOpt = userStorage.findOne(user.getId());
        if (storedUserOpt.isEmpty()) {
            log.warn("Try to update non existed user, user id = {}", user.getId());
            throw new NotFoundException(String.format("User does not exists, id = %d", user.getId()));
        }

        final User storedUser = storedUserOpt.get();
        storedUser.setName(user.getName());
        storedUser.setEmail(user.getEmail());
        storedUser.setLogin(user.getLogin());
        storedUser.setBirthday(user.getBirthday());

        final Optional<User> updatedUserOpt = userStorage.update(storedUser);
        // check if user was alredy removed
        if (updatedUserOpt.isEmpty()) {
            log.warn("Try to update non existed user, user id = {}", user.getId());
            throw new NotFoundException(String.format("User does not exists, id = %d", user.getId()));
        }

        return updatedUserOpt.get();
    }

    public User findOne(final int id) {
        final Optional<User> userOpt = userStorage.findOne(id);
        if (userOpt.isEmpty()) {
            log.warn("Try to get non existed user, user id = {}", id);
            throw new NotFoundException(String.format("User does not exists, id = %d", id));
        }
        return userOpt.get();
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public Collection<User> findFriends(final int id) {
        final Optional<User> userOpt = userStorage.findOne(id);
        if (userOpt.isEmpty()) {
            log.warn("Try to get friends for non existed user, user id = {}", id);
            throw new NotFoundException(String.format("User does not exists, id = %d", id));
        }

        return userOpt.get().getFriends()
            .stream()
            .map(userStorage::findOne)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    public Collection<User> findCommonFriends(final int id, final int otherId) {
        final Optional<User> userOpt = userStorage.findOne(id);
        if (userOpt.isEmpty()) {
            log.warn("Try to get mutual friends for non existed user, user id = {}", id);
            throw new NotFoundException(String.format("User does not exists, id = %d", id));
        }

        final Optional<User> otherUserOpt = userStorage.findOne(otherId);
        if (userOpt.isEmpty()) {
            log.warn("Try to get mutual friends for non existed user, user id = {}", otherId);
            throw new NotFoundException(String.format("User does not exists, id = %d", otherId));
        }

        final Set<Integer> userFriends = userOpt.get().getFriends();
        final Set<Integer> otherUserFriends = otherUserOpt.get().getFriends();

        return userFriends.stream()
            .filter(otherUserFriends::contains)
            .map(userStorage::findOne)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    public void addFriend(final int userId, final int friendId) {
        final Optional<User> userOpt = userStorage.findOne(userId);
        if (userOpt.isEmpty()) {
            log.warn("Try to add friend on behalf of non existed user, user id = {}", userId);
            throw new NotFoundException(String.format("User does not exists, id = %d", userId));
        }

        final Optional<User> friendOpt = userStorage.findOne(friendId);
        if (friendOpt.isEmpty()) {
            log.warn("Try to add non existed user as friend, user id = {}", friendId);
            throw new NotFoundException(String.format("User does not exists, id = %d", friendId));
        }

        final User user = userOpt.get();
        final User friend = friendOpt.get();

        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());

        userStorage.update(user);
        userStorage.update(friend);
    }

    public void removeFriend(final int userId, final int friendId) {
        final Optional<User> userOpt = userStorage.findOne(userId);
        if (userOpt.isEmpty()) {
            log.warn("Try to remove friend on behalf of non existed user, user id = {}", userId);
            throw new NotFoundException(String.format("User does not exists, id = %d", userId));
        }

        final Optional<User> friendOpt = userStorage.findOne(friendId);
        if (friendOpt.isEmpty()) {
            log.warn("Try to remove non existed user from friends, user id = {}", friendId);
            throw new NotFoundException(String.format("User does not exists, id = %d", friendId));
        }

        final User user = userOpt.get();
        final User friend = friendOpt.get();

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
