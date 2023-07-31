package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.model.Film;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films;
    private int nextId;

    public InMemoryFilmStorage() {
        this.films = new LinkedHashMap<>();
        this.nextId = 1;
    }

    public Film create(final Film film) {
        setId(film);
        films.put(film.getId(), film);
        return film;
    }

    public Film update(final Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Try to update not existed film: {}", film);
            throw new UpdateException("Not found");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film findOne(int id) {
        return films.get(id);
    }

    public Collection<Film> findAll() {
        return films.values();
    }

    private void setId(final Film fild) {
        fild.setId(nextId);
        nextId++;
    }
}
