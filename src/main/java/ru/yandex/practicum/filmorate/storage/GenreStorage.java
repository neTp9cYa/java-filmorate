package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.Genre;

public interface GenreStorage {
    List<Genre> findAll();

    Optional<Genre> findById(final int id);

    List<Genre> findByIds(final int[] ids);
}
