package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    User create(final User user);

    User update(final User user);

    User findOne(final int id);

    Collection<User> findAll();
}
