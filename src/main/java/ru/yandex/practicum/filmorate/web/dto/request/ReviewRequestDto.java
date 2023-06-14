package ru.yandex.practicum.filmorate.web.dto.request;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
public class ReviewRequestDto {
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
}

