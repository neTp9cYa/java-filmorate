package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    User create(final User user);

    void update(final User user);

    Optional<User> findUserById(final int id);

    Collection<User> findAll();

    void addFriend(final int userId, final int friendId);

    void removeFriend(final int userId, final int friendId);

    Collection<User> findFriends(final int id);

    Collection<User> findCommonFriends(final int id, final int otherId);
}
