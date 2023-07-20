package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@RequestBody final Film film) {
        if (films.containsKey(film.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody final Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }
}
