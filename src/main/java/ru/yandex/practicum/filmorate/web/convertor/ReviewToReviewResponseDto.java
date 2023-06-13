package ru.yandex.practicum.filmorate.web.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.web.dto.response.ReviewResponseDto;

@Component
public class ReviewToReviewResponseDto implements Converter<Review, ReviewResponseDto> {
    @Override
    public ReviewResponseDto convert(Review review) {
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto();
        reviewResponseDto.setReviewId(review.getId());
        reviewResponseDto.setContent(review.getContent());
        reviewResponseDto.setIsPositive(review.getIsPositive());
        reviewResponseDto.setUseful(review.getUseful());
        reviewResponseDto.setUserId(review.getUserId());
        reviewResponseDto.setFilmId(review.getFilmId());
        return reviewResponseDto;
    }
}
