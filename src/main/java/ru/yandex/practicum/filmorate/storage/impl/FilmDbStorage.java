package ru.yandex.practicum.filmorate.storage.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Film create(final Film film) {
        final String sql = "insert into FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID, RATE, LIKE_COUNT) " +
            "values(:NAME, :DESCRIPTION, :RELEASE_DATE, :DURATION, :MPA_ID, :RATE, :LIKE_COUNT)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("NAME", film.getName())
            .addValue("DESCRIPTION", film.getDescription())
            .addValue("RELEASE_DATE", film.getReleaseDate())
            .addValue("DURATION", film.getDuration())
            .addValue("MPA_ID", film.getMpa().getId())
            .addValue("RATE", film.getRate())
            .addValue("LIKE_COUNT", 0);
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcOperations.update(sql, parameters, keyHolder, new String[] {"FILM_ID"});

        final int filmId = keyHolder.getKey().intValue();
        film.setId(filmId);

        insertFilmGenres(filmId, film.getGenres());

        return film;
    }

    @Override
    public void update(Film film) {
        final String sql = "update FILMS set " +
            "NAME = :NAME, DESCRIPTION = :DESCRIPTION, RELEASE_DATE = :RELEASE_DATE, " +
            "DURATION = :DURATION, MPA_ID = :MPA_ID, RATE = :RATE " +
            "where FILM_ID = :FILM_ID";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("NAME", film.getName())
            .addValue("DESCRIPTION", film.getDescription())
            .addValue("RELEASE_DATE", film.getReleaseDate())
            .addValue("DURATION", film.getDuration())
            .addValue("MPA_ID", film.getMpa().getId())
            .addValue("RATE", film.getRate())
            .addValue("FILM_ID", film.getId());

        namedParameterJdbcOperations.update(sql, parameters);

        deleteFilmGenres(film.getId());
        insertFilmGenres(film.getId(), film.getGenres());
    }

    private void deleteFilmGenres(final int filmId) {
        final String sql = "delete from FILM_GENRES where FILM_ID = :FILM_ID";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("FILM_ID", filmId);

        namedParameterJdbcOperations.update(sql, parameters);
    }

    private void insertFilmGenres(final int filmId, Set<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            return;
        }

        final String sql = "insert into FILM_GENRES (FILM_ID, GENRE_ID) values(:FILM_ID, :GENRE_ID)";
        final SqlParameterSource[] paremeters = genres.stream()
            .map(genre -> new MapSqlParameterSource()
                .addValue("FILM_ID", filmId, Types.INTEGER)
                .addValue("GENRE_ID", genre.getId(), Types.INTEGER))
            .toArray(SqlParameterSource[]::new);
        namedParameterJdbcOperations.batchUpdate(sql, paremeters);
    }

    @Override
    public Optional<Film> findFilmById(int id) {
        final String sql =
            "select f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, f.RATE, f.LIKE_COUNT, " +
                "m.NAME as MPA_NAME, m.DESCRIPTION as MPA_DESCRIPTION " +
                "from FILMS f " +
                "left join MPAS m on f.MPA_ID = m.MPA_ID " +
                "where f.FILM_ID = :FILM_ID";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("FILM_ID", id);

        try {
            final Film film = namedParameterJdbcOperations.queryForObject(sql, parameters, this::mapRowToFilm);
            film.setGenres(new TreeSet(findGenresByFilmId(film.getId())));
            return Optional.of(film);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private List<Genre> findGenresByFilmId(final int filmId) {
        final String sql = "select g.GENRE_ID, g.NAME " +
            "from FILM_GENRES fg " +
            "left join GENRES g on fg.GENRE_ID = g.GENRE_ID " +
            "where fg.FILM_ID = :FILM_ID " +
            "order by g.GENRE_ID";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("FILM_ID", filmId);
        final List<Genre> genres = namedParameterJdbcOperations.query(sql, parameters, this::mapRowToGenre);
        return genres;
    }

    private Film mapRowToFilm(final ResultSet row, final int rowNumber) throws SQLException {
        final Film.FilmBuilder filmBuilder = Film.builder()
            .id(row.getInt("FILM_ID"))
            .name(row.getString("NAME"))
            .description(row.getString("DESCRIPTION"))
            .releaseDate(Optional.ofNullable(row.getDate("RELEASE_DATE")).map(d -> d.toLocalDate()).orElse(null))
            .duration(row.getInt("DURATION"))
            .mpa(Mpa.builder().id(row.getInt("MPA_ID")).build())
            .rate(row.getInt("RATE"))
            .likeCount(row.getInt("LIKE_COUNT"));

        if (row.getString("MPA_NAME") != null) {
            final Mpa mpa = Mpa.builder()
                .id(row.getInt("MPA_ID"))
                .name(row.getString("MPA_NAME"))
                .build();
            filmBuilder.mpa(mpa);
        }

        return filmBuilder.build();
    }

    @Override
    public List<Film> findAll() {
        final String sql =
            "select f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, f.RATE, f.LIKE_COUNT, " +
                "m.NAME as MPA_NAME, m.DESCRIPTION as MPA_DESCRIPTION " +
                "from FILMS f " +
                "left join MPAS m on f.MPA_ID = m.MPA_ID " +
                "order by f.FILM_ID";
        final List<Film> films = namedParameterJdbcOperations.getJdbcOperations().query(sql, this::mapRowToFilm);

        final List<Integer> filmIds = films.stream().map(f -> f.getId()).collect(Collectors.toList());
        final Map<Integer, SortedSet<Genre>> genres = findGenresByFilmIds(filmIds);
        for (final Film film : films) {
            final SortedSet<Genre> filmGenres = genres.get(film.getId());
            film.setGenres(filmGenres != null ? filmGenres : new TreeSet<>());
        }

        return films;
    }

    private Map<Integer, SortedSet<Genre>> findGenresByFilmIds(final List<Integer> ids) {
        final String sql = "select fg.FILM_ID, g.GENRE_ID, g.NAME as GENRE_NAME " +
            "from FILM_GENRES fg " +
            "inner join GENRES g on fg.GENRE_ID = g.GENRE_ID " +
            "where fg.FILM_ID IN (:FILM_IDS)";
        final SqlParameterSource parameters = new MapSqlParameterSource().addValue("FILM_IDS", ids);
        return namedParameterJdbcOperations.query(sql, parameters, resultSet -> {
            final Map<Integer, SortedSet<Genre>> filmsToGenres = new HashMap<>();
            final Map<Integer, Genre> genres = new HashMap<>();

            while (resultSet.next()) {
                final int filmId = resultSet.getInt("FILM_ID");
                final int genreId = resultSet.getInt("GENRE_ID");

                Genre genre = genres.get(genreId);
                if (genre == null) {
                    genre = Genre.builder()
                        .id(genreId)
                        .name(resultSet.getString("GENRE_NAME"))
                        .build();
                    genres.put(genreId, genre);
                }

                SortedSet<Genre> filmToGenres = filmsToGenres.get(filmId);
                if (filmToGenres == null) {
                    filmToGenres = new TreeSet<>();
                    filmsToGenres.put(filmId, filmToGenres);
                }

                filmToGenres.add(genre);
            }

            return filmsToGenres;
        });
    }

    private Map<Integer, Genre> findGengresByIds(final Set<Integer> ids) {
        final String sql = "select GENRE_ID, NAME from GENRES where GENRE_ID IN (:GENRE_IDS)";
        final SqlParameterSource parameters = new MapSqlParameterSource().addValue("GENRE_IDS", ids);
        final List<Genre> genres = namedParameterJdbcOperations.query(sql, parameters, this::mapRowToGenre);
        return genres.stream().collect(Collectors.toMap(g -> g.getId(), g -> g));
    }

    private Genre mapRowToGenre(final ResultSet row, final int rowNumber) throws SQLException {
        return Genre.builder()
            .id(row.getInt("GENRE_ID"))
            .name(row.getString("NAME"))
            .build();
    }

    @Override
    public List<Film> findPopular(final int count) {
        final String sql =
            "select f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, f.RATE, f.LIKE_COUNT, " +
                "m.NAME as MPA_NAME, m.DESCRIPTION as MPA_DESCRIPTION " +
                "from FILMS f " +
                "left join MPAS m on f.MPA_ID = m.MPA_ID " +
                "order by f.LIKE_COUNT desc, f.FILM_ID " +
                "limit :count";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("count", count);
        final List<Film> films = namedParameterJdbcOperations.query(sql, parameters, this::mapRowToFilm);

        final List<Integer> filmIds = films.stream().map(f -> f.getId()).collect(Collectors.toList());
        final Map<Integer, SortedSet<Genre>> genres = findGenresByFilmIds(filmIds);
        for (final Film film : films) {
            final SortedSet<Genre> filmGenres = genres.get(film.getId());
            film.setGenres(filmGenres != null ? filmGenres : new TreeSet<>());
        }

        return films;
    }

    @Override
    public void addLike(final int filmId, final int userId) {
        final String sql = "merge into LIKES l " +
            "using (values (:FILM_ID, :USER_ID)) tmp (FILM_ID, USER_ID) " +
            "on l.FILM_ID = tmp.FILM_ID and l.USER_ID = tmp.USER_ID " +
            "when not matched then INSERT (FILM_ID, USER_ID) values (tmp.FILM_ID, tmp.USER_ID)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("FILM_ID", filmId)
            .addValue("USER_ID", userId);
        final int insertRowCount = namedParameterJdbcOperations.update(sql, parameters);

        if (insertRowCount == 1) {
            updateLikeCount(filmId);
        }
    }

    @Override
    public void removeLike(final int filmId, final int userId) {
        final String sql = "delete from LIKES where FILM_ID = :FILM_ID and USER_ID = :USER_ID";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("FILM_ID", filmId)
            .addValue("USER_ID", userId);
        final int deletedRowCount = namedParameterJdbcOperations.update(sql, parameters);

        if (deletedRowCount == 1) {
            updateLikeCount(filmId);
        }
    }

    private void updateLikeCount(final int filmId) {
        final String filmSql = "update FILMS " +
            "set LIKE_COUNT = (select count(USER_ID) from LIKES where FILM_ID = :FILM_ID) " +
            "where FILM_ID = :FILM_ID";
        final SqlParameterSource filmParameters = new MapSqlParameterSource()
            .addValue("FILM_ID", filmId);
        namedParameterJdbcOperations.update(filmSql, filmParameters);
    }
}
