package ru.yandex.practicum.filmorate.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.web.dto.request.ReviewRequestDto;

@Mapper
public interface ReviewMapper {
    @Mapping(target = "id", source = "reviewId")
    Review mapToReview(ReviewRequestDto dto);
}
