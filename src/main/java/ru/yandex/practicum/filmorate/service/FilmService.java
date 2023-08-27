package ru.yandex.practicum.filmorate.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmValidator filmValidator;
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public Film create(final Film film) {
        filmValidator.validate(film);
        transform(film);
        return filmStorage.create(film);
    }

    public Film update(final Film film) {
        // check if film exists
        filmStorage.findFilmById(film.getId())
            .orElseThrow(() -> new NotFoundException(String.format("Film does not exists, id = %d", film.getId())));

        filmValidator.validate(film);
        transform(film);
        filmStorage.update(film);
        return film;
    }

    private void transform(Film film) {
        if (film.getGenres() == null) {
            film.setGenres(new ArrayList<>());
            return;
        }

        final Set<Integer> processedGenreIds = new HashSet<>();
        final List<Genre> uniqGenres = new ArrayList<>();
        for (final Genre genre : film.getGenres()) {
            if (processedGenreIds.contains(genre.getId())) {
                continue;
            }
            uniqGenres.add(genre);
            processedGenreIds.add(genre.getId());
        }
        film.setGenres(uniqGenres);
    }

    public Film findOne(final int id) {
        return filmStorage.findFilmById(id)
            .orElseThrow(() -> new NotFoundException(String.format("Film does not exists, id = %d", id)));
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Collection<Film> findPopular(final int count) {
        return filmStorage.findPopular(count);
    }

    public void addLike(final int filmId, final int userId) {
        final Film film = filmStorage.findFilmById(filmId)
            .orElseThrow(() -> new NotFoundException(String.format("Film does not exists, id = %d", filmId)));

        final User user = userStorage.findUserById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User does not exists, id = %d", userId)));

        filmStorage.addLike(film.getId(), user.getId());
    }

    public void removeLike(final int filmId, final int userId) {
        final Film film = filmStorage.findFilmById(filmId)
            .orElseThrow(() -> new NotFoundException(String.format("Film does not exists, id = %d", filmId)));

        final User user = userStorage.findUserById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User does not exists, id = %d", userId)));

        filmStorage.removeLike(film.getId(), user.getId());
    }
}
