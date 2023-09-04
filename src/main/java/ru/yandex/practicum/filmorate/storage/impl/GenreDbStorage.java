package ru.yandex.practicum.filmorate.storage.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public List<Genre> findAll() {
        final String sql = "select GENRE_ID, NAME from GENRES order by GENRE_ID";
        final List<Genre> genres = namedParameterJdbcOperations.getJdbcOperations().query(sql, this::mapRowToGenre);
        return genres;
    }

    @Override
    public Optional<Genre> findById(final int id) {
        final String sql = "select GENRE_ID, NAME from GENRES where GENRE_ID = :GENRE_ID";
        final MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("GENRE_ID", id);
        try {
            final Genre genre = namedParameterJdbcOperations.queryForObject(sql, parameters, this::mapRowToGenre);
            return Optional.of(genre);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> findByIds(final int[] ids) {
        final String sql = "select ID, NAME from GENRES where GENRE_ID IN (:GENRE_IDS)";
        final MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("GENRE_IDS", ids);
        final List<Genre> genres = namedParameterJdbcOperations.getJdbcOperations().query(sql, this::mapRowToGenre);
        return genres;
    }

    private Genre mapRowToGenre(final ResultSet row, final int rowNumber) throws SQLException {
        return Genre.builder()
            .id(row.getInt("GENRE_ID"))
            .name(row.getString("NAME"))
            .build();
    }
}
