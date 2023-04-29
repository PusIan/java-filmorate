package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film extends Entity {
    private final Set<Integer> likes = new HashSet<>();
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
}
