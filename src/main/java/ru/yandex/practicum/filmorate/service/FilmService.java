package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UpdateException;
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
        final Film createdFilm = filmStorage.create(film);
        return createdFilm;
    }

    public Film update(final Film film) {
        filmValidator.validate(film);
        final Film updatedFilm = filmStorage.update(film);
        return updatedFilm;
    }

    public Film findOne(final int id) {
        return filmStorage.findOne(id);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Collection<Film> findTop(final int limit) {
        return filmStorage.findTop(limit);
    }

    public void addLike(final int userId, final int filmId) {
        final User user = userStorage.findOne(userId);
        if (user == null) {
            log.warn("Try to add like on behalf of non existed user, user id = {}", userId);
            throw new UpdateException(String.format("User does not exists, id = %d", userId));
        }

        final Film film = filmStorage.findOne(filmId);
        if (user == null) {
            log.warn("Try to add like to non existed film, film id = {}", filmId);
            throw new UpdateException(String.format("Film does not exists, id = %d", filmId));
        }

        film.getLikes().add(filmId);
        filmStorage.update(film);
    }

    public void removeLike(final int userId, final int filmId) {
        final User user = userStorage.findOne(userId);
        if (user == null) {
            log.warn("Try to remove like on behalf of non existed user, user id = {}", userId);
            throw new UpdateException(String.format("User does not exists, id = %d", userId));
        }

        final Film film = filmStorage.findOne(filmId);
        if (user == null) {
            log.warn("Try to add like from non existed film, film id = {}", filmId);
            throw new UpdateException(String.format("Film does not exists, id = %d", filmId));
        }

        final Set<Integer> likes = film.getLikes();
        likes.remove(filmId);
        film.setLikes(likes);
        filmStorage.update(film);
    }
}
