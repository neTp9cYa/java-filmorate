package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.EntityExistsException;
import ru.yandex.practicum.filmorate.exception.EntityNotExistsException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@RequestBody final Film film) {
        validate(film);
        if (films.containsKey(film.getId())) {
            log.warn("Try to create film with existed id: {}", film);
            throw new EntityExistsException();
        }
        films.put(film.getId(), film);
        log.warn("Film was created: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody final Film film) {
        validate(film);
        if (!films.containsKey(film.getId())) {
            log.warn("Try to update not existed film: {}", film);
            throw new EntityNotExistsException();
        }
        films.put(film.getId(), film);
        log.warn("Film was updated: {}", film);
        return film;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    private void validate(final Film film) {

        // название не может быть пустым
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Film name is not valid: {}", film);
            throw new ValidationException();
        }

        // максимальная длина описания — 200 символов
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.warn("Film description is not valid: {}", film);
            throw new ValidationException();
        }

        // дата релиза — не раньше 28 декабря 1895 года;
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Film release date  is not valid: {}", film);
            throw new ValidationException();
        }

        // продолжительность фильма должна быть положительной.
        if (film.getDuration() <= 0) {
            log.warn("Film duration is not valid: {}", film);
            throw new ValidationException();
        }
    }
}
