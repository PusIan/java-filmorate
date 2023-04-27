package ru.yandex.practicum.filmorate.web.validator.utils;

import ru.yandex.practicum.filmorate.web.dto.request.UserRequestDto;

import java.time.LocalDate;

public class UserValidatorTestUtils {
    public static UserRequestDto getUserRequestDto(Integer id, String email, String login,
                                                   String name, LocalDate birthday) {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setId(id);
        userRequestDto.setEmail(email);
        userRequestDto.setLogin(login);
        userRequestDto.setName(name);
        userRequestDto.setBirthday(birthday);
        return userRequestDto;
    }
}
