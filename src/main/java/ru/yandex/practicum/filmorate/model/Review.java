package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
public class Review extends Entity {
    private int reviewId;
    @NonNull
    @NotBlank
    private String content;
    private boolean isPositive;
    @NonNull
    @Positive
    private Integer userId;
    @NonNull
    @Positive
    private Integer filmId;
    private int useful;
}
