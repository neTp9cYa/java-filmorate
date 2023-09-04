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
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public List<Mpa> findAll() {
        final String sql = "select MPA_ID, NAME, DESCRIPTION from MPAS order by MPA_ID";
        final List<Mpa> mpas = namedParameterJdbcOperations.getJdbcOperations().query(sql, this::mapRowToMpa);
        return mpas;
    }

    @Override
    public Optional<Mpa> findById(final int id) {
        final String sql = "select MPA_ID, NAME, DESCRIPTION from MPAS where MPA_ID = :MPA_ID";
        final MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("MPA_ID", id);
        try {
            final Mpa mpa = namedParameterJdbcOperations.queryForObject(sql, parameters, this::mapRowToMpa);
            return Optional.of(mpa);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Mpa mapRowToMpa(final ResultSet row, final int rowNumber) throws SQLException {
        return Mpa.builder()
            .id(row.getInt("MPA_ID"))
            .name(row.getString("NAME"))
            .build();
    }
}
