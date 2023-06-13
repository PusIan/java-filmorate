package ru.yandex.practicum.filmorate.web.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.web.dto.response.UserResponseDto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserToUserResponseDto implements Converter<User, UserResponseDto> {
    @Override
    public UserResponseDto convert(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setLogin(user.getLogin());
        userResponseDto.setName(user.getName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setBirthday(user.getBirthday().toLocalDate());
        return userResponseDto;
    }

    public Collection<UserResponseDto> getListResponse(List<User> userList) {
        return userList.stream()
                .map(user -> convert(user))
                .collect(Collectors.toList());
    }
}
