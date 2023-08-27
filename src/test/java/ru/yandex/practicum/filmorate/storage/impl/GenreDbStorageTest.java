package ru.yandex.practicum.filmorate.storage.impl;

import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("Store genre in database")
class GenreDbStorageTest {

    private final GenreDbStorage genreStorage;

    @Test
    @DisplayName("Get all genres should success")
    void findAll() {
        Collection<Genre> mpas = genreStorage.findAll();

        assertNotNull(mpas);
        assertEquals(6, mpas.size());
    }

    @Test
    @DisplayName("Get genre by id should success")
    void findById() {
        Optional<Genre> mpaOptional = genreStorage.findById(1);

        assertThat(mpaOptional)
            .isPresent()
            .hasValueSatisfying(mpa -> {
                    assertEquals(1, mpa.getId());
                    assertEquals("Комедия", mpa.getName());
                }
            );
    }
}