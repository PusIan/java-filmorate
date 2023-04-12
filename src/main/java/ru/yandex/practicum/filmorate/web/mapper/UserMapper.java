package ru.yandex.practicum.filmorate.web.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.web.dto.request.UserRequestDto;

@Mapper
public interface UserMapper {

    User mapToUser(UserRequestDto dto);
}
