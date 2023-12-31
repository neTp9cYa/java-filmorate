package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmValidator filmValidator;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film create(final Film film) {
        filmValidator.validate(film);
        return filmStorage.create(film);
    }

    public Film update(final Film film) {
        // check if film exists
        filmStorage.findOne(film.getId())
            .orElseThrow(() -> new NotFoundException(String.format("Film does not exists, id = %d", film.getId())));

        filmValidator.validate(film);
        filmStorage.update(film);
        return film;
    }

    public Film findOne(final int id) {
        return filmStorage.findOne(id)
            .orElseThrow(() -> new NotFoundException(String.format("Film does not exists, id = %d", id)));
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Collection<Film> findPopular(final int count) {
        return filmStorage.findPopular(count);
    }

    public void addLike(final int filmId, final int userId) {
        final Film film = filmStorage.findOne(filmId)
            .orElseThrow(() -> new NotFoundException(String.format("Film does not exists, id = %d", filmId)));

        final User user = userStorage.findOne(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User does not exists, id = %d", userId)));

        filmStorage.addLike(film.getId(), user.getId());
    }

    public void removeLike(final int filmId, final int userId) {
        final Film film = filmStorage.findOne(filmId)
            .orElseThrow(() -> new NotFoundException(String.format("Film does not exists, id = %d", filmId)));

        final User user = userStorage.findOne(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User does not exists, id = %d", userId)));

        filmStorage.removeLike(film.getId(), user.getId());
    }
}
