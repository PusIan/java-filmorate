package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, of = {"id"})
public class Review extends Entity {
    private Integer id;
    private String content;
    private Boolean isPositive;
    private Integer userId;
    private Integer filmId;
    private int useful;
}
