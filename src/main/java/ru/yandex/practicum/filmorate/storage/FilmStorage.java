package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
    Film create(final Film film);

    Optional<Film> update(final Film film);

    Optional<Film> findOne(final int id);

    Collection<Film> findAll();

    Collection<Film> findPopular(final int count);
}
