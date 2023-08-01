package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
    Film create(final Film film);

    Film update(final Film film);

    Film findOne(final int id);

    Collection<Film> findAll();

    Collection<Film> findPopular(final int count);
}
