package ru.yandex.practicum.filmorate.storage.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> findAll() {
        final String sql = "select ID, NAME from GENRE order by ID";
        final SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);

        Collection<Genre> mpas = new ArrayList<>();
        while (rows.next()) {
            mpas.add(mapRowToGenre(rows));
        }

        return mpas;
    }

    @Override
    public Optional<Genre> findById(final int id) {
        final String sql = "select ID, NAME from GENRE where ID = ?";
        final SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, id);

        if (!rows.next()) {
            return Optional.empty();
        }

        final Genre genre = mapRowToGenre(rows);
        return Optional.of(genre);
    }

    private Genre mapRowToGenre(final SqlRowSet row) {
        return Genre.builder()
            .id(row.getInt("ID"))
            .name(row.getString("NAME"))
            .build();
    }
}
