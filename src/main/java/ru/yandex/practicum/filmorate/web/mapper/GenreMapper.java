package ru.yandex.practicum.filmorate.web.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.web.dto.request.GenreRequestDto;

@Mapper
public interface GenreMapper {

    Genre mapToGenre(GenreRequestDto dto);
}
