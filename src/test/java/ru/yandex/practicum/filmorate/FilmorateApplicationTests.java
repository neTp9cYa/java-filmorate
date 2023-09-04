package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase
@DisplayName("Spring configuration")
class FilmorateApplicationTests {

    @Test
    @DisplayName("Context is loaded successfully")
    void contextLoads() {
    }
}
