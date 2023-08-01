package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import java.util.Optional;
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
        filmValidator.validate(film);

        final Optional<Film> storedFilmOpt = filmStorage.findOne(film.getId());
        if (storedFilmOpt.isEmpty()) {
            log.warn("Try to update non existed film, film id = {}", film.getId());
            throw new NotFoundException(String.format("Film does not exists, id = %d", film.getId()));
        }

        final Film storedFilm = storedFilmOpt.get();
        storedFilm.setName(film.getName());
        storedFilm.setDescription(film.getDescription());
        storedFilm.setDuration(film.getDuration());
        storedFilm.setReleaseDate(film.getReleaseDate());

        final Optional<Film> updatedFilmOpt = filmStorage.update(storedFilm);
        // check if film was alredy removed
        if (updatedFilmOpt.isEmpty()) {
            log.warn("Try to update non existed film, film id = {}", film.getId());
            throw new NotFoundException(String.format("Film does not exists, id = %d", film.getId()));
        }

        return updatedFilmOpt.get();
    }

    public Film findOne(final int id) {
        final Optional<Film> filmOpt = filmStorage.findOne(id);
        if (filmOpt.isEmpty()) {
            log.warn("Try to get non existed film, film id = {}", id);
            throw new NotFoundException(String.format("Film does not exists, id = %d", id));
        }
        return filmOpt.get();
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Collection<Film> findPopular(final int count) {
        return filmStorage.findPopular(count);
    }

    public void addLike(final int userId, final int filmId) {
        final Optional<User> userOpt = userStorage.findOne(userId);
        if (userOpt.isEmpty()) {
            log.warn("Try to add like on behalf of non existed user, user id = {}", userId);
            throw new NotFoundException(String.format("User does not exists, id = %d", userId));
        }

        final Optional<Film> filmOpt = filmStorage.findOne(filmId);
        if (filmOpt.isEmpty()) {
            log.warn("Try to add like to non existed film, film id = {}", filmId);
            throw new NotFoundException(String.format("Film does not exists, id = %d", filmId));
        }

        final Film film = filmOpt.get();
        final User user = userOpt.get();

        film.getLikes().add(user.getId());
        filmStorage.update(film);
    }

    public void removeLike(final int userId, final int filmId) {
        final Optional<User> userOpt = userStorage.findOne(userId);
        if (userOpt.isEmpty()) {
            log.warn("Try to remove like on behalf of non existed user, user id = {}", userId);
            throw new NotFoundException(String.format("User does not exists, id = %d", userId));
        }

        final Optional<Film> filmOpt = filmStorage.findOne(filmId);
        if (filmOpt.isEmpty()) {
            log.warn("Try to add like from non existed film, film id = {}", filmId);
            throw new NotFoundException(String.format("Film does not exists, id = %d", filmId));
        }

        final Film film = filmOpt.get();
        final User user = userOpt.get();

        film.getLikes().remove(filmId);
        filmStorage.update(film);
    }
}
