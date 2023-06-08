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

    /**
     * <a href="https://stackoverflow.com/questions/49702336/jdbctemplate-boolean-not-mapping-properly-with-beanpropertyrowmapper">...</a>
     */
    public boolean isIsPositive() {
        return isPositive;
    }

    public void setIsPositive(boolean positive) {
        isPositive = positive;
    }

    @Override
    public Integer getId() {
        return getReviewId();
    }
}
