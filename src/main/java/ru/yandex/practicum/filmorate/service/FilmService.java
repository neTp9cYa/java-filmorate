package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
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
        filmStorage.update(film);
        return film;
    }

    public Film findOne(final int id) {
        final Optional<Film> filmOpt = filmStorage.findOne(id);
        if (filmOpt.isEmpty()) {
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

    public void addLike(final int filmId, final int userId) {
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(final int filmId, final int userId) {
        filmStorage.removeLike(filmId, userId);
    }
}
