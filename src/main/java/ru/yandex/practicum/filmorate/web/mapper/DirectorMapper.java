package ru.yandex.practicum.filmorate.web.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.web.dto.request.DirectorRequestDto;
import ru.yandex.practicum.filmorate.web.dto.request.MpaRequestDto;

@Mapper
public interface DirectorMapper {

    Director mapToDirector(DirectorRequestDto dto);
}
