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
import ru.yandex.practicum.filmorate.model.Genre;

@JdbcTest
@Import(GenreDbStorage.class)
@DisplayName("Store genre in database")
class GenreDbStorageTest {

    @Autowired
    private GenreDbStorage genreStorage;

    @Test
    @DisplayName("Get all genres should success")
    void findAll() {
        List<Genre> genres = genreStorage.findAll();

        assertNotNull(genres);
        assertEquals(6, genres.size(), "Genres count incorrect");
    }

    @Test
    @DisplayName("Get genre by id should success")
    void findById() {
        Optional<Genre> genreOptional = genreStorage.findById(1);

        assertThat(genreOptional)
            .isPresent()
            .hasValueSatisfying(genre -> {
                    assertEquals(1, genre.getId(), "Id of retrived gengre is invalid");
                    assertEquals("Комедия", genre.getName(), "Name of retrived gengre is invalid");
                }
            );
    }

    @Test
    @DisplayName("Find genre by incorrect id should return null")
    void findGenreByIncorrectIdShoudReturnNull() {
        final Optional<Genre> genreOptional = genreStorage.findById(-1);

        assertThat(genreOptional).isNotPresent();
    }
}