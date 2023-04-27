package ru.yandex.practicum.filmorate.web.controller;

import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.web.dto.request.UserRequestDto;
import ru.yandex.practicum.filmorate.web.dto.response.UserResponseDto;
import ru.yandex.practicum.filmorate.web.mapper.UserMapper;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private final ConversionService conversionService;
    private final UserMapper userMapper;
    private int currentId = 1;

    public UserController(ConversionService conversionService, UserMapper userMapper) {
        this.conversionService = conversionService;
        this.userMapper = userMapper;
    }

    private int getCurrentId() {
        return currentId++;
    }

    @GetMapping
    public Collection<UserResponseDto> findAll() {

        return users.values().stream()
                .map(user -> conversionService.convert(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping
    public UserResponseDto create(@Valid @RequestBody UserRequestDto userRequestDto) {
        int id = getCurrentId();
        userRequestDto.setId(id);
        users.put(userRequestDto.getId(), userMapper.mapToUser(userRequestDto));
        return conversionService.convert(users.get(id), UserResponseDto.class);
    }

    @PutMapping
    public UserResponseDto update(@Valid @RequestBody UserRequestDto userRequestDto) {
        if (!users.containsKey(userRequestDto.getId())) {
            throw new NotFoundException(String.format("User with id %s not found", userRequestDto.getId()));
        }
        users.put(userRequestDto.getId(), userMapper.mapToUser(userRequestDto));
        return conversionService.convert(users.get(userRequestDto.getId()), UserResponseDto.class);
    }
}
