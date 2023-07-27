package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmValidator filmValidator;
    private final FilmStorage filmStorage;

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

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }
}
