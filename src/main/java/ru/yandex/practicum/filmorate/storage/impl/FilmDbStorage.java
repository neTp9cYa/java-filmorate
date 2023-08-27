package ru.yandex.practicum.filmorate.storage.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Component
@Qualifier("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(final Film film) {
        final SimpleJdbcInsert filmInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("FILM")
            .usingGeneratedKeyColumns("ID");

        final int filmId = filmInsert.executeAndReturnKey(
            new HashMap<>() {{
                put("NAME", film.getName());
                put("DESCRIPTION", film.getDescription());
                put("RELEASE_DATE", film.getReleaseDate());
                put("DURATION", film.getDuration());
                put("MPA_ID", film.getMpa().getId());
                put("RATE", film.getRate());
                put("LIKE_COUNT", 0);
            }}
        ).intValue();

        film.setId(filmId);

        insertFilmGenres(filmId, film.getGenres());

        return film;
    }

    @Override
    public void update(Film film) {
        final String filmUpdateSql = "update FILM set " +
            "NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, " +
            "DURATION = ?, MPA_ID = ?, RATE = ? " +
            "where ID = ?";

        jdbcTemplate.update(filmUpdateSql,
            film.getName(), film.getDescription(), film.getReleaseDate(),
            film.getDuration(), film.getMpa().getId(), film.getRate(),
            film.getId());

        deleteFilmGenres(film.getId());
        insertFilmGenres(film.getId(), film.getGenres());
    }

    private void deleteFilmGenres(final int filmId) {
        final String sql = "delete from FILM_GENRE where FILM_ID = ?";
        jdbcTemplate.update(sql, filmId);
    }

    private void insertFilmGenres(final int filmId, Collection<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            return;
        }

        SimpleJdbcInsert filmGengreInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("FILM_GENRE");
        for (final Genre genre : genres) {
            filmGengreInsert.execute(new HashMap<>() {{
                put("FILM_ID", filmId);
                put("GENRE_ID", genre.getId());
            }});
        }
    }

    @Override
    public Optional<Film> findFilmById(int id) {
        final String sql =
            "select f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, f.RATE, f.LIKE_COUNT, " +
                "m.NAME as MPA_NAME, m.DESCRIPTION as MPA_DESCRIPTION " +
                "from FILM f " +
                "left join MPA m on f.MPA_ID = m.ID " +
                "where f.ID = ?";
        final SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, id);

        if (!rows.next()) {
            return Optional.empty();
        }

        final Film film = mapRowToFilm(rows);
        film.setGenres(findGenresByFilmId(film.getId()));
        return Optional.of(film);
    }

    private Film mapRowToFilm(SqlRowSet row) {
        final Film.FilmBuilder filmBuilder = Film.builder()
            .id(row.getInt("id"))
            .name(row.getString("name"))
            .description(row.getString("description"))
            .releaseDate(row.getDate("release_date").toLocalDate())
            .duration(row.getInt("duration"))
            .mpa(Mpa.builder().id(row.getInt("mpa_id")).build())
            .rate(row.getInt("rate"))
            .likeCount(row.getInt("LIKE_COUNT"));

        if (row.getString("MPA_NAME") != null) {
            final Mpa mpa = Mpa.builder()
                .id(row.getInt("MPA_ID"))
                .name(row.getString("MPA_NAME"))
                .description(row.getString("MPA_DESCRIPTION"))
                .build();
            filmBuilder.mpa(mpa);
        }

        return filmBuilder.build();
    }

    @Override
    public Collection<Film> findAll() {
        final String sql =
            "select f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, f.RATE, f.LIKE_COUNT, " +
                "m.NAME as MPA_NAME, m.DESCRIPTION as MPA_DESCRIPTION " +
                "from FILM f " +
                "left join MPA m on f.MPA_ID = m.ID " +
                "order by f.ID";
        final SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);

        final Collection<Film> films = new ArrayList<>();
        while (rows.next()) {
            final Film film = mapRowToFilm(rows);
            film.setGenres(findGenresByFilmId(film.getId()));
            films.add(film);
        }

        return films;
    }

    private Collection<Genre> findGenresByFilmId(final int filmId) {
        final String sql = "select g.ID, g.NAME " +
            "from FILM_GENRE fg " +
            "left join GENRE g on fg.GENRE_ID = g.ID " +
            "where FILM_ID = ? " +
            "order by g.ID";
        final SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, filmId);

        final Collection<Genre> genres = new ArrayList<>();
        while (rows.next()) {
            genres.add(mapRowToGenre(rows));
        }

        return genres;
    }

    private Genre mapRowToGenre(SqlRowSet rows) {
        return Genre.builder()
            .id(rows.getInt("id"))
            .name(rows.getString("name"))
            .build();
    }

    @Override
    public Collection<Film> findPopular(int count) {
        final String sql =
            "select f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, f.RATE, f.LIKE_COUNT, " +
                "m.NAME as MPA_NAME, m.DESCRIPTION as MPA_DESCRIPTION " +
                "from FILM f " +
                "left join MPA m on f.MPA_ID = m.ID " +
                "order by f.LIKE_COUNT desc, f.ID " +
                "limit ?";
        final SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, count);

        final Collection<Film> films = new ArrayList<>();
        while (rows.next()) {
            final Film film = mapRowToFilm(rows);
            film.setGenres(findGenresByFilmId(film.getId()));
            films.add(film);
        }

        return films;
    }

    @Override
    public void addLike(int filmId, int userId) {
        final SimpleJdbcInsert likeInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("FILMORATE_LIKE");
        likeInsert.execute(new HashMap<>() {{
            put("FILM_ID", filmId);
            put("USER_ID", userId);
        }});

        final String filmUpdate = "update FILM set LIKE_COUNT = LIKE_COUNT + 1 where ID = ?";
        jdbcTemplate.update(filmUpdate, filmId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        final String sql = "delete from FILMORATE_LIKE where FILM_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);

        final String filmUpdate = "update FILM set LIKE_COUNT = LIKE_COUNT - 1 where ID = ?";
        jdbcTemplate.update(filmUpdate, filmId);
    }
}
