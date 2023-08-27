package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
public class Genre {
    private int id;
    private String name;
}
