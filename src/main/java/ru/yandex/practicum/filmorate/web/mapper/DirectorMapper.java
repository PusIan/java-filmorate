package ru.yandex.practicum.filmorate.web.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.web.dto.request.DirectorRequestDto;

@Mapper
public interface DirectorMapper {

    Director mapToDirector(DirectorRequestDto dto);
}
