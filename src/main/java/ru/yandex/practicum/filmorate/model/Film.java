package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.SortedSet;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.ToString;

@Data
@ToString
@Builder
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Mpa mpa;
    private int rate;
    private int likeCount;
    @Singular
    private SortedSet<Genre> genres;
}