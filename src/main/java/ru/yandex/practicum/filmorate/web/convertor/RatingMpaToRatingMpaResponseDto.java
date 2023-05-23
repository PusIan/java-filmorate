package ru.yandex.practicum.filmorate.web.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.web.dto.response.RatingMpaResponseDto;

@Component
public class RatingMpaToRatingMpaResponseDto implements Converter<RatingMpa, RatingMpaResponseDto> {
    @Override
    public RatingMpaResponseDto convert(RatingMpa ratingMpa) {
        RatingMpaResponseDto ratingMpaResponseDto = new RatingMpaResponseDto();
        ratingMpaResponseDto.setId(ratingMpa.getId());
        ratingMpaResponseDto.setName(ratingMpa.getName());
        return ratingMpaResponseDto;
    }
}
