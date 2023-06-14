package ru.yandex.practicum.filmorate.web.dto.response;

import lombok.Data;

@Data
public class ReviewResponseDto {
    private Integer reviewId;
    private String content;
    private Boolean isPositive;
    private Integer userId;
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
