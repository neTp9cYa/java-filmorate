package ru.yandex.practicum.filmorate.storage.impl;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;

@JdbcTest
@Import(MpaDbStorage.class)
@DisplayName("Store genre in database")
class MpaDbStorageTest {

    @Autowired
    private MpaDbStorage mpaStorage;

    @Test
    @DisplayName("Get all mpa should success")
    void findAll() {
        List<Mpa> mpas = mpaStorage.findAll();

        assertNotNull(mpas);
        assertEquals(5, mpas.size(), "Mpa count incorrect");
    }

    @Test
    @DisplayName("Get mpa by id should success")
    void findById() {
        Optional<Mpa> mpaOptional = mpaStorage.findById(1);

        assertThat(mpaOptional)
            .isPresent()
            .hasValueSatisfying(mpa -> {
                    assertEquals(1, mpa.getId(), "Id of retrived mpa is invalid");
                    assertEquals("G", mpa.getName(), "Name of retrived mpa is invalid");
                }
            );
    }

    @Test
    @DisplayName("Find mpa by incorrect id should return null")
    void findFilmByIncorrectIdShoudReturnNull() {
        final Optional<Mpa> mpaOptional = mpaStorage.findById(-1);
        assertThat(mpaOptional).isNotPresent();
    }
}