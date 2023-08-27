package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

@Component
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public Collection<Mpa> findAll() {
        return mpaStorage.findAll();
    }
    public Mpa findById(final int id) {
        return mpaStorage.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("Mpa does not exists, id = %d", id)));
    }
}
