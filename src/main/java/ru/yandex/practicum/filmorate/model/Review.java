package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Review extends Entity {
    private Integer reviewId;
    @NonNull
    @NotBlank
    private String content;
    @NonNull
    private Boolean isPositive;
    @NonNull
    private Integer userId;
    @NonNull
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review)) return false;
        Review review = (Review) o;
        return getUseful() == review.getUseful() &&
                getContent().equals(review.getContent()) &&
                isIsPositive() == review.isIsPositive() &&
                getUserId().equals(review.getUserId()) &&
                getFilmId().equals(review.getFilmId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewId);
    }
}
