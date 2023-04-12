package ru.yandex.practicum.filmorate.web.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.web.dto.request.FilmRequestDto;

@Mapper
public interface FilmMapper {

    Film mapToFilm(FilmRequestDto dto);
}
