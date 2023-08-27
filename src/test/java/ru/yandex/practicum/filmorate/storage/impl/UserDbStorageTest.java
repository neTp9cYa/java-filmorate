package ru.yandex.practicum.filmorate.storage.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("Store genre in database")
class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    @DisplayName("Create user and then get by id should success")
    void createAndFindById() {
        User user = getCorrectUserBuilder().build();
        user = userStorage.create(user);

        assertTrue(user.getId() > 0);

        final Optional<User> storedUserOptional = userStorage.findUserById(user.getId());

        assertThat(storedUserOptional).isPresent();
        assertEquals(user, storedUserOptional.get());
    }

    @Test
    @DisplayName("Update user should success")
    void update() {
        User user = getCorrectUserBuilder().build();
        user = userStorage.create(user);

        user.setName("new");
        user.setEmail("new@new.new");
        user.setLogin("new");
        user.setBirthday(LocalDate.of(2010, 10, 10));

        userStorage.update(user);

        final Optional<User> storedUserOptional = userStorage.findUserById(user.getId());

        assertThat(storedUserOptional).isPresent();
        assertEquals(user, storedUserOptional.get());

    }

    @Test
    @DisplayName("Find all users should success")
    void findAll() {
        User user1 = getCorrectUserBuilder().name("name1").build();
        User user2 = getCorrectUserBuilder().name("name2").build();

        user1 = userStorage.create(user1);
        user2 = userStorage.create(user2);

        final Collection<User> users = userStorage.findAll();

        assertThat(users)
            .isNotNull()
            .contains(user1, user2);
    }

    @Test
    @DisplayName("Add friends should success")
    void addFriend() {
        User user1 = getCorrectUserBuilder().name("name1").build();
        User user2 = getCorrectUserBuilder().name("name2").build();

        user1 = userStorage.create(user1);
        user2 = userStorage.create(user2);

        userStorage.addFriend(user1.getId(), user2.getId());

        Collection<User> user1Friends = userStorage.findFriends(user1.getId());

        assertNotNull(user1Friends);
        assertEquals(1, user1Friends.size());
        assertEquals(user2, user1Friends.stream().findFirst().get());

        Collection<User> user2Friends = userStorage.findFriends(user2.getId());
        assertNotNull(user2Friends);
        assertEquals(0, user2Friends.size());
    }

    @Test
    @DisplayName("Remove friend should success")
    void removeFriend() {
        User user1 = getCorrectUserBuilder().name("name1").build();
        User user2 = getCorrectUserBuilder().name("name2").build();

        user1 = userStorage.create(user1);
        user2 = userStorage.create(user2);

        userStorage.addFriend(user1.getId(), user2.getId());
        userStorage.addFriend(user2.getId(), user1.getId());
        userStorage.removeFriend(user2.getId(), user1.getId());

        Collection<User> user1Friends = userStorage.findFriends(user1.getId());

        assertNotNull(user1Friends);
        assertEquals(1, user1Friends.size());
        assertEquals(user2, user1Friends.stream().findFirst().get());


        Collection<User> user2Friends = userStorage.findFriends(user2.getId());
        assertNotNull(user2Friends);
        assertEquals(0, user2Friends.size());
    }

    @Test
    @DisplayName("Find friends should success")
    void findFriends() {
        User user1 = getCorrectUserBuilder().name("name1").build();
        User user2 = getCorrectUserBuilder().name("name2").build();
        User user3 = getCorrectUserBuilder().name("name3").build();

        user1 = userStorage.create(user1);
        user2 = userStorage.create(user2);
        user3 = userStorage.create(user3);

        userStorage.addFriend(user1.getId(), user2.getId());
        userStorage.addFriend(user1.getId(), user3.getId());

        Collection<User> friends = userStorage.findFriends(user1.getId());

        assertNotNull(friends);
        assertEquals(2, friends.size());

        final List<User> usersList = new ArrayList<>(friends);
        assertEquals(user2, usersList.get(0));
        assertEquals(user3, usersList.get(1));
    }

    @Test
    @DisplayName("Find common friend should success")
    void findCommonFriends() {
        User user1 = getCorrectUserBuilder().name("name1").build();
        User user2 = getCorrectUserBuilder().name("name2").build();
        User user3 = getCorrectUserBuilder().name("name3").build();

        user1 = userStorage.create(user1);
        user2 = userStorage.create(user2);
        user3 = userStorage.create(user3);

        userStorage.addFriend(user1.getId(), user3.getId());
        userStorage.addFriend(user2.getId(), user3.getId());

        Collection<User> friends = userStorage.findCommonFriends(user1.getId(), user2.getId());

        assertNotNull(friends);
        assertThat(friends)
            .isNotNull()
            .hasSize(1)
            .contains(user3);
    }

    private User.UserBuilder getCorrectUserBuilder() {
        return User.builder()
            .name("Test")
            .email("test@test.test")
            .login("test")
            .birthday(LocalDate.of(2000, 1, 1));
    }
}