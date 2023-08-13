package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users;

    private Map<Integer, Set<Integer>> friends = new HashMap<>();
    private int nextId;

    public InMemoryUserStorage() {
        this.users = new LinkedHashMap<>();
        this.nextId = 1;
    }

    @Override
    public User create(final User user) {
        setId(user);
        users.put(user.getId(), user);
        friends.put(user.getId(), new HashSet<>());
        return user;
    }

    @Override
    public void update(final User user) {
        users.replace(user.getId(), user);
    }

    @Override
    public Optional<User> findOne(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public void addFriend(final int userId, final int friendId) {
        friends.get(userId).add(friendId);
    }

    @Override
    public void removeFriend(final int userId, final int friendId) {
        friends.get(userId).remove(friendId);
    }

    @Override
    public Collection<User> findFriends(int id) {
        return friends.get(id)
            .stream()
            .map(users::get)
            .collect(Collectors.toList());
    }

    @Override
    public Collection<User> findCommonFriends(final int id, final int otherId) {
        final Set<Integer> userFriends = friends.get(id);
        final Set<Integer> otherUserFriends = friends.get(otherId);

        return friends.get(id).stream()
            .filter(otherUserFriends::contains)
            .map(users::get)
            .collect(Collectors.toList());
    }

    private void setId(final User user) {
        user.setId(nextId);
        nextId++;
    }
}
