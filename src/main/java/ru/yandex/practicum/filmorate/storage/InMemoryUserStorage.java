package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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

    @Override
    public User create(final User user) {
        setId(user);
        setDefaults(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> update(final User user) {
        if (!users.containsKey(user.getId())) {
            return Optional.empty();
        }
        setDefaults(user);
        users.put(user.getId(), user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> findOne(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    private void setId(final User user) {
        user.setId(nextId);
        nextId++;
    }

    private void setDefaults(final User user) {
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
    }
}
