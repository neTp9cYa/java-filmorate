package ru.yandex.practicum.filmorate.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film create(@RequestBody final Film film) {
        log.info("Start to process of filn creation request: {}", film);
        final Film createddFilm = filmService.create(film);
        log.info("End to process of filn creation request: {}", film);
        return createddFilm;
    }

    @PutMapping
    public Film update(@RequestBody final Film film) {
        log.info("Start to process of film update request: {}", film);
        final Film updatedFilm = filmService.update(film);
        log.info("End to process of film update request: {}", film);
        return updatedFilm;
    }

    @GetMapping("/{id}")
    public Film findOne(@PathVariable final int id) {
        return filmService.findOne(id);
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/popular")
    public List<Film> findPopular(@RequestParam(defaultValue = "10") final int count) {
        return filmService.findPopular(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") final int filmId,
                        @PathVariable final int userId) {
        log.info("Start to process of add like request: film id {} and user id {}", filmId, userId);
        filmService.addLike(filmId, userId);
        log.info("End to process of add like request: film id {} and user id {}", filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") final int filmId,
                           @PathVariable final int userId) {
        log.info("Start to process of remove like request: film id {} and user id {}", filmId, userId);
        filmService.removeLike(filmId, userId);
        log.info("End to process of remove like request: film id {} and user id {}", filmId, userId);
    }

}
