package ru.yandex.practicum.filmorate.storage.impl;

import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("Store genre in database")
class MpaDbStorageTest {

    private final MpaDbStorage mpaStorage;

    @Test
    @DisplayName("Get all mpa should success")
    void findAll() {
        Collection<Mpa> mpas = mpaStorage.findAll();

        assertNotNull(mpas);
        assertEquals(5, mpas.size());
    }

    @Test
    @DisplayName("Get mpa by id should success")
    void findById() {
        Optional<Mpa> mpaOptional = mpaStorage.findById(1);

        assertThat(mpaOptional)
            .isPresent()
            .hasValueSatisfying(mpa -> {
                    assertEquals(1, mpa.getId());
                    assertEquals("G", mpa.getName());
                    assertEquals("у фильма нет возрастных ограничений", mpa.getDescription());
                }
            );
    }
}