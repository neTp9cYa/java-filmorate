package ru.yandex.practicum.filmorate.storage.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public User create(User user) {
        final String sql = "insert into USERS (EMAIL, LOGIN, BIRTHDAY, NAME) "
            + "values (:EMAIL, :LOGIN, :BIRTHDAY, :NAME)";
        final MapSqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("EMAIL", user.getEmail())
            .addValue("LOGIN", user.getLogin())
            .addValue("BIRTHDAY", user.getBirthday())
            .addValue("NAME", user.getName());
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcOperations.update(sql, parameters, keyHolder, new String[] {"USER_ID"});

        final int userId = keyHolder.getKey().intValue();
        user.setId(userId);

        return user;
    }

    @Override
    public void update(User user) {
        final String sql = "update USERS set " +
            "EMAIL = :EMAIL, LOGIN = :LOGIN, BIRTHDAY = :BIRTHDAY, NAME = :NAME " +
            "where USER_ID = :USER_ID";
        final MapSqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("EMAIL", user.getEmail())
            .addValue("LOGIN", user.getLogin())
            .addValue("BIRTHDAY", user.getBirthday())
            .addValue("NAME", user.getName())
            .addValue("USER_ID", user.getId());

        namedParameterJdbcOperations.update(sql, parameters);
    }

    @Override
    public Optional<User> findUserById(int id) {
        final String sql = "select USER_ID, EMAIL, LOGIN, BIRTHDAY, NAME from USERS where USER_ID = :USER_ID";
        final MapSqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("USER_ID", id);

        try {
            final User user = namedParameterJdbcOperations.queryForObject(sql, parameters, this::mapRowToUser);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private User mapRowToUser(final ResultSet row, final int rowNumber) throws SQLException {
        return User.builder()
            .id(row.getInt("USER_ID"))
            .email(row.getString("EMAIL"))
            .login(row.getString("LOGIN"))
            .birthday(row.getDate("BIRTHDAY").toLocalDate())
            .name(row.getString("NAME"))
            .build();
    }

    @Override
    public List<User> findAll() {
        final String sql = "select USER_ID, EMAIL, LOGIN, BIRTHDAY, NAME from USERS order by USER_ID";
        final List<User> users = namedParameterJdbcOperations.getJdbcOperations().query(sql, this::mapRowToUser);
        return users;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        final String sql = "insert into FRIENDS (USER_ID, FRIEND_ID) values (:USER_ID, :FRIEND_ID)";
        final MapSqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("USER_ID", userId)
            .addValue("FRIEND_ID", friendId);

        namedParameterJdbcOperations.update(sql, parameters);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        final String sql = "delete from FRIENDS where USER_ID = :USER_ID and FRIEND_ID = :FRIEND_ID";
        final MapSqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("USER_ID", userId)
            .addValue("FRIEND_ID", friendId);

        namedParameterJdbcOperations.update(sql, parameters);
    }

    @Override
    public List<User> findFriends(int id) {
        final String sql = "select u.USER_ID, u.EMAIL, u.LOGIN, u.BIRTHDAY, u.NAME " +
            "from FRIENDS f " +
            "inner join USERS u on f.FRIEND_ID = u.USER_ID " +
            "where f.USER_ID = :USER_ID " +
            "order by u.USER_ID";
        final MapSqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("USER_ID", id);
        final List<User> users = namedParameterJdbcOperations.query(sql, parameters, this::mapRowToUser);
        return users;
    }

    @Override
    public List<User> findCommonFriends(int id, int otherId) {
        final String sql = "select u.USER_ID, u.EMAIL, u.LOGIN, u.BIRTHDAY, u.NAME " +
            "from FRIENDS f1 " +
            "inner join FRIENDS f2 on f1.FRIEND_ID = f2.FRIEND_ID " +
            "inner join USERS u on f1.FRIEND_ID = u.USER_ID " +
            "where f1.USER_ID = :USER_ID and f2.USER_ID = :OTHER_USER_ID " +
            "order by u.USER_ID";
        final MapSqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("USER_ID", id)
            .addValue("OTHER_USER_ID", otherId);
        final List<User> users = namedParameterJdbcOperations.query(sql, parameters, this::mapRowToUser);
        return users;
    }
}
