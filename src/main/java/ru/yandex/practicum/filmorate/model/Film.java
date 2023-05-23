package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film extends Entity {
    Integer id;
    String name;
    String description;
    Date releaseDate;
    Integer duration;
    List<Genre> genres;
    RatingMpa mpa;
}
