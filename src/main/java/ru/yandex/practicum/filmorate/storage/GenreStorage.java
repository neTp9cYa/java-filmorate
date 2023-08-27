package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.Genre;

public interface GenreStorage {
    Collection<Genre> findAll();

    Optional<Genre> findById(final int id);
}
