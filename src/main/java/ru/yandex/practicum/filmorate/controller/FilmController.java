package ru.yandex.practicum.filmorate.controller;

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
import ru.yandex.practicum.filmorate.exception.EntityNotExistsException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;
    private final FilmValidator filmValidator = new FilmValidator();

    @PostMapping
    public Film create(@RequestBody final Film film) {
        filmValidator.validate(film);
        setId(film);
        films.put(film.getId(), film);
        log.warn("Film was created: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody final Film film) {
        filmValidator.validate(film);
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

    private void setId(final Film fild) {
        fild.setId(nextId);
        nextId++;
    }
}
