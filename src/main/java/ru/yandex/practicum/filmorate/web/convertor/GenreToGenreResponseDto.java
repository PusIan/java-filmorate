package ru.yandex.practicum.filmorate.web.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.web.dto.response.GenreResponseDto;

@Component
public class GenreToGenreResponseDto implements Converter<Genre, GenreResponseDto> {
    @Override
    public GenreResponseDto convert(Genre genre) {
        GenreResponseDto genreResponseDto = new GenreResponseDto();
        genreResponseDto.setId(genre.getId());
        genreResponseDto.setName(genre.getName());
        return genreResponseDto;
    }
}
