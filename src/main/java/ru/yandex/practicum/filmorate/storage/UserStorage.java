package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    User create(final User user);

    void update(final User user);

    Optional<User> findUserById(final int id);

    List<User> findAll();

    void addFriend(final int userId, final int friendId);

    void removeFriend(final int userId, final int friendId);

    List<User> findFriends(final int id);

    List<User> findCommonFriends(final int id, final int otherId);
}
