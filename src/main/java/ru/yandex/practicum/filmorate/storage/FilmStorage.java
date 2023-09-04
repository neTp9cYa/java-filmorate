package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
    Film create(final Film film);

    void update(final Film film);

    Optional<Film> findFilmById(final int id);

    List<Film> findAll();

    List<Film> findPopular(final int count);

    void addLike(final int filmId, final int userId);

    void removeLike(final int filmId, final int userId);
}
