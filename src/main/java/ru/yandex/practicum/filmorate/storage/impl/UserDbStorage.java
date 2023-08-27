package ru.yandex.practicum.filmorate.storage.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Component
@Qualifier("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        final SimpleJdbcInsert userInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("FILMORATE_USER")
            .usingGeneratedKeyColumns("ID");

        final int userId = userInsert.executeAndReturnKey(new HashMap<>() {{
            put("EMAIL", user.getEmail());
            put("LOGIN", user.getLogin());
            put("BIRTHDAY", user.getBirthday());
            put("NAME", user.getName());
        }}).intValue();

        user.setId(userId);

        return user;
    }

    @Override
    public void update(User user) {
        final String sql = "update FILMORATE_USER set " +
            "EMAIL = ?, LOGIN = ?, BIRTHDAY = ?, NAME = ? " +
            "where ID = ?";

        jdbcTemplate.update(sql,
            user.getEmail(), user.getLogin(), user.getBirthday(), user.getName(),
            user.getId());
    }

    @Override
    public Optional<User> findUserById(int id) {
        final String sql = "select ID, EMAIL, LOGIN, BIRTHDAY, NAME from FILMORATE_USER where ID = ?";
        final SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, id);

        if (!rows.next()) { return Optional.empty(); }

        final User user = mapRowToUser(rows);

        return Optional.of(user);
    }

    private User mapRowToUser(SqlRowSet row) {
        return User.builder()
            .id(row.getInt("ID"))
            .email(row.getString("EMAIL"))
            .login(row.getString("LOGIN"))
            .birthday(row.getDate("BIRTHDAY").toLocalDate())
            .name(row.getString("NAME"))
            .build();
    }

    @Override
    public Collection<User> findAll() {
        final String sql = "select ID, EMAIL, LOGIN, BIRTHDAY, NAME from FILMORATE_USER order by ID";
        final SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);

        final Collection<User> users = new ArrayList<>();
        while (rows.next()) {
            users.add(mapRowToUser(rows));
        }

        return users;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        final SimpleJdbcInsert friendInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("FRIEND");
        friendInsert.execute(new HashMap<>() {{
            put("USER_ID", userId);
            put("FRIEND_ID", friendId);
        }});
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        final String sql = "delete from FRIEND where USER_ID = ? and FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public Collection<User> findFriends(int id) {
        final String sql = "select u.ID, u.EMAIL, u.LOGIN, u.BIRTHDAY, u.NAME " +
            "from FRIEND f " +
            "inner join FILMORATE_USER u on f.FRIEND_ID = u.ID " +
            "where f.USER_ID = ? " +
            "order by u.ID";
        final SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, id);

        final Collection<User> users = new ArrayList<>();
        while (rows.next()) {
            users.add(mapRowToUser(rows));
        }

        return users;
    }

    @Override
    public Collection<User> findCommonFriends(int id, int otherId) {
        final String sql = "select u.ID, u.EMAIL, u.LOGIN, u.BIRTHDAY, u.NAME " +
            "from FRIEND f1 " +
            "inner join FRIEND f2 on f1.FRIEND_ID = f2.FRIEND_ID " +
            "inner join FILMORATE_USER u on f1.FRIEND_ID = u.ID " +
            "where f1.USER_ID = ? and f2.USER_ID = ? " +
            "order by u.ID";
        final SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, id, otherId);

        final Collection<User> users = new ArrayList<>();
        while (rows.next()) {
            users.add(mapRowToUser(rows));
        }

        return users;
    }
}
