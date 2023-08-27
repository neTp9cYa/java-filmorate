package ru.yandex.practicum.filmorate.storage.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public Collection<Mpa> findAll() {
        final String sql = "select ID, NAME, DESCRIPTION from MPA order by ID";
        final SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);

        Collection<Mpa> mpas = new ArrayList<>();
        while (rows.next()) {
            mpas.add(mapRowToMpa(rows));
        }

        return mpas;
    }

    @Override
    public Optional<Mpa> findById(final int id) {
        final String sql = "select ID, NAME, DESCRIPTION from MPA where ID = ?";
        final SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, id);

        if (!rows.next()) { return Optional.empty(); }

        final Mpa mpa = mapRowToMpa(rows);
        return Optional.of(mpa);
    }

    private Mpa mapRowToMpa(final SqlRowSet row) {
        return Mpa.builder()
            .id(row.getInt("ID"))
            .name(row.getString("NAME"))
            .description(row.getString("DESCRIPTION"))
            .build();
    }
}
