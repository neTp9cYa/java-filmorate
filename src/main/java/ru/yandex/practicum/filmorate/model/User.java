package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Integer> friends;
}