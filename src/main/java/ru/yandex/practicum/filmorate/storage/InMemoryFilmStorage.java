package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films;
    private final Set<Film> popularFilms;
    private int nextId;

    public InMemoryFilmStorage() {
        this.films = new LinkedHashMap<>();
        this.nextId = 1;
        this.popularFilms = new TreeSet<>((changedFilm, storedFilm) -> {
            if (storedFilm.getId() == changedFilm.getId()) {
                return 0;
            }
            if (storedFilm.getLikes().size() != changedFilm.getLikes().size()) {
                return storedFilm.getLikes().size() - changedFilm.getLikes().size();
            }
            return storedFilm.getId() - changedFilm.getId();
        });
    }

    @Override
    public Film create(final Film film) {
        setId(film);
        setDefaults(film);
        films.put(film.getId(), film);
        popularFilms.add(film);
        return film;
    }

    @Override
    public Optional<Film> update(final Film film) {
        if (!films.containsKey(film.getId())) {
            return Optional.empty();
        }
        setDefaults(film);
        films.put(film.getId(), film);
        popularFilms.add(film);
        return Optional.of(film);
    }

    @Override
    public Optional<Film> findOne(int id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Collection<Film> findPopular(final int count) {
        return popularFilms.stream().limit(count).collect(Collectors.toList());
    }

    private void setId(final Film fild) {
        fild.setId(nextId);
        nextId++;
    }

    private void setDefaults(final Film film) {
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
    }
}
