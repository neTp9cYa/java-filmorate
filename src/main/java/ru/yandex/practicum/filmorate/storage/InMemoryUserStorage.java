package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.model.User;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users;
    private int nextId;

    public InMemoryUserStorage() {
        this.users = new LinkedHashMap<>();
        this.nextId = 1;
    }

    public User create(final User user) {
        setId(user);
        users.put(user.getId(), user);
        return user;
    }

    public User update(final User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Try to update not existed user: {}", user);
            throw new UpdateException("Not found");
        }
        users.put(user.getId(), user);
        return user;
    }

    public Collection<User> findAll() {
        return users.values();
    }

    private void setId(final User user) {
        user.setId(nextId);
        nextId++;
    }
}
