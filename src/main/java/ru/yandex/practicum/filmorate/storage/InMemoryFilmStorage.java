package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.HashMap;
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
    private final Set<Integer> popularFilms;
    private Map<Integer, Set<Integer>> likes = new HashMap<>();
    private int nextId;

    public InMemoryFilmStorage() {
        this.films = new LinkedHashMap<>();
        this.nextId = 1;
        this.popularFilms = new TreeSet<>((changedFilmId, storedFilmId) -> {
            if (storedFilmId == changedFilmId) {
                return 0;
            }
            final Set<Integer> storedFilmLikes = likes.get(storedFilmId);
            final Set<Integer> changedFilmLikes = likes.get(changedFilmId);
            if (storedFilmLikes.size() != changedFilmLikes.size()) {
                return storedFilmLikes.size() - changedFilmLikes.size();
            }
            return storedFilmId - changedFilmId;
        });
    }

    @Override
    public Film create(final Film film) {
        setId(film);
        films.put(film.getId(), film);
        likes.put(film.getId(), new HashSet<>());
        popularFilms.add(film.getId());
        return film;
    }

    @Override
    public void update(final Film film) {
        films.replace(film.getId(), film);
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
        return popularFilms
            .stream()
            .limit(count)
            .map(films::get)
            .collect(Collectors.toList());
    }

    @Override
    public void addLike(final int filmId, final int userId) {
        popularFilms.remove(filmId);
        likes.get(filmId).add(userId);
        popularFilms.add(filmId);
    }

    @Override
    public void removeLike(final int filmId, final int userId) {
        popularFilms.remove(filmId);
        likes.get(filmId).remove(userId);
        popularFilms.add(filmId);
    }

    private void setId(final Film fild) {
        fild.setId(nextId);
        nextId++;
    }
}
