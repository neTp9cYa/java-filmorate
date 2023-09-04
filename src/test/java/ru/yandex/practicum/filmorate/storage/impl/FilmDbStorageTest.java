package ru.yandex.practicum.filmorate.storage.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

@JdbcTest
@Import({FilmDbStorage.class, UserDbStorage.class})
@DisplayName("Store film in database")
class FilmDbStorageTest {

    @Autowired
    private FilmDbStorage filmStorage;
    @Autowired
    private UserDbStorage userStorage;

    @Test
    @DisplayName("Create film and then get by id should success")
    void createAndFindById() {
        Film film = getCorrectFilmBuilder().build();
        film = filmStorage.create(film);

        assertNotNull(film, "Created film is null");
        assertNotNull(film.getId(), "Film id is not generated");

        final Optional<Film> storedFilmOptional = filmStorage.findFilmById(film.getId());

        assertThat(storedFilmOptional).isPresent();
        assertEquals(film, storedFilmOptional.get(), "Сreated film contains invalid data");
    }

    @Test
    @DisplayName("Find film by incorrect id should return null")
    void findFilmByIncorrectIdShoudReturnNull() {
        final Optional<Film> storedFilmOptional = filmStorage.findFilmById(-1);
        assertThat(storedFilmOptional).isNotPresent();
    }

    @Test
    @DisplayName("Update film should success")
    void update() {
        Film film = getCorrectFilmBuilder().build();
        film = filmStorage.create(film);

        film.setName("new name");
        film.setDuration(100);
        film.setDescription("new description");
        film.setRate(200);
        film.setReleaseDate(LocalDate.of(2010, 10, 10));
        film.setMpa(Mpa.builder()
            .id(2)
            .name("PG")
            .description("детям рекомендуется смотреть фильм с родителями")
            .build());
        SortedSet<Genre> genres = new TreeSet<>();
        genres.add(Genre.builder().id(3).name("Мультфильм").build());
        film.setGenres(genres);

        filmStorage.update(film);
        final Optional<Film> storedFilmOptional = filmStorage.findFilmById(film.getId());

        assertThat(storedFilmOptional).isPresent();
        assertEquals(film, storedFilmOptional.get(), "Film properties updated incorrectly");
    }

    @Test
    @DisplayName("Find all films should success")
    void findAll() {
        Film film1 = getCorrectFilmBuilder().name("name1").build();
        Film film2 = getCorrectFilmBuilder().name("name2").build();

        film1 = filmStorage.create(film1);
        film2 = filmStorage.create(film2);

        final List<Film> films = filmStorage.findAll();

        assertThat(films)
            .isNotNull()
            .contains(film1, film2);
    }

    @Test
    @DisplayName("Find popular films should success")
    void findPopular() {
        User user1 = getCorrectUserBuilder().name("name1").build();
        User user2 = getCorrectUserBuilder().name("name2").build();
        User user3 = getCorrectUserBuilder().name("name3").build();

        user1 = userStorage.create(user1);
        user2 = userStorage.create(user2);
        user3 = userStorage.create(user3);

        Film film1 = getCorrectFilmBuilder().name("name1").build();
        Film film2 = getCorrectFilmBuilder().name("name2").build();

        film1 = filmStorage.create(film1);
        film2 = filmStorage.create(film2);

        filmStorage.addLike(film1.getId(), user1.getId());
        filmStorage.addLike(film1.getId(), user2.getId());
        film1 = filmStorage.findFilmById(film1.getId()).get();

        List<Film> films = filmStorage.findPopular(1);
        assertThat(films)
            .isNotNull()
            .hasSize(1)
            .contains(film1);

        filmStorage.addLike(film2.getId(), user1.getId());
        filmStorage.addLike(film2.getId(), user2.getId());
        filmStorage.addLike(film2.getId(), user3.getId());
        film2 = filmStorage.findFilmById(film2.getId()).get();

        films = filmStorage.findPopular(1);
        assertThat(films)
            .isNotNull()
            .hasSize(1)
            .contains(film2);
    }

    @Test
    @DisplayName("Add and remove like should success")
    void addAndRemoveLike() {
        User user = getCorrectUserBuilder().build();
        user = userStorage.create(user);

        Film film = getCorrectFilmBuilder().build();
        film = filmStorage.create(film);

        filmStorage.addLike(film.getId(), user.getId());
        Film storedFilm = filmStorage.findFilmById(film.getId()).get();
        assertEquals(1, storedFilm.getLikeCount(), "Like has not added");

        filmStorage.removeLike(film.getId(), user.getId());
        storedFilm = filmStorage.findFilmById(film.getId()).get();
        assertEquals(0, storedFilm.getLikeCount(), "Like has not removed");
    }

    private Film.FilmBuilder getCorrectFilmBuilder() {
        return Film.builder()
            .name("Test")
            .duration(10)
            .description("test")
            .rate(4)
            .releaseDate(LocalDate.of(2000, 1, 1))
            .mpa(Mpa.builder()
                .id(1)
                .name("G")
                .description("у фильма нет возрастных ограничений")
                .build())
            .genre(Genre.builder().id(1).name("Комедия").build())
            .genre(Genre.builder().id(2).name("Драма").build());
    }

    private User.UserBuilder getCorrectUserBuilder() {
        return User.builder()
            .name("Test")
            .email("test@test.test")
            .login("test")
            .birthday(LocalDate.of(2000, 1, 1));
    }
}