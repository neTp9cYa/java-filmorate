package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films;
    private final FilmValidator filmValidator;
    private int nextId;

    public FilmController() {
        films = new LinkedHashMap<>();
        filmValidator = new FilmValidator();
        nextId = 1;
    }

    @PostMapping
    public Film create(@RequestBody final Film film) {
        log.trace("Film is creating: {}", film);
        filmValidator.validate(film);
        setId(film);
        films.put(film.getId(), film);
        log.trace("Film is created: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody final Film film) {
        log.trace("Film is updating: {}", film);
        filmValidator.validate(film);
        if (!films.containsKey(film.getId())) {
            log.warn("Try to update not existed film: {}", film);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        films.put(film.getId(), film);
        log.trace("Film is updated: {}", film);
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
