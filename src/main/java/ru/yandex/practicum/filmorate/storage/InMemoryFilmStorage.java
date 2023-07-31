package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.model.Film;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films;
    private final Set<Film> topFilms;
    private int nextId;

    public InMemoryFilmStorage() {
        this.films = new LinkedHashMap<>();
        this.nextId = 1;
        this.topFilms = new TreeSet<>((film1, film2) -> {
            if (film1.getId() == film2.getId()) { return 0; }

            if (film1.getLikes().size() >= film2.getLikes().size()) { return 1; }
            else { return -1; }
        });
    }

    @Override
    public Film create(final Film film) {
        setId(film);
        films.put(film.getId(), film);
        topFilms.add(film);
        return film;
    }

    @Override
    public Film update(final Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Try to update not existed film: {}", film);
            throw new UpdateException("Not found");
        }
        films.put(film.getId(), film);
        topFilms.add(film);
        return film;
    }

    @Override
    public Film findOne(int id) {
        return films.get(id);
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Collection<Film> findTop(final int limit) {
        return topFilms.stream().limit(limit).collect(Collectors.toList());
    }

    private void setId(final Film fild) {
        fild.setId(nextId);
        nextId++;
    }
}
