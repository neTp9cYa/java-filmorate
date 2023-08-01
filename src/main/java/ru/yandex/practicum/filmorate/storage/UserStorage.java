package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    User create(final User user);

    Optional<User> update(final User user);

    Optional<User> findOne(final int id);

    Collection<User> findAll();
}
