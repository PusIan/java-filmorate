package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, of = {"reviewId"})
public class Review extends Entity {
    private Integer reviewId;
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

    public boolean isIsPositive() {
        return isPositive;
    }

}
