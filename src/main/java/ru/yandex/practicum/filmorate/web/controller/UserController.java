package ru.yandex.practicum.filmorate.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.web.dto.request.EventRequestDto;
import ru.yandex.practicum.filmorate.web.dto.request.UserRequestDto;
import ru.yandex.practicum.filmorate.web.dto.response.EventResponseDto;
import ru.yandex.practicum.filmorate.web.dto.response.FilmResponseDto;
import ru.yandex.practicum.filmorate.web.dto.response.UserResponseDto;
import ru.yandex.practicum.filmorate.web.mapper.UserMapper;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {
    private final ConversionService conversionService;
    private final UserMapper userMapper;
    private final UserService userService;


    @GetMapping
    public Collection<UserResponseDto> getAll() {

        return userService.getAll().stream()
                .map(user -> conversionService.convert(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserResponseDto getById(@PathVariable int userId) {
        return conversionService.convert(userService.getById(userId), UserResponseDto.class);
    }

    @PostMapping
    public UserResponseDto create(@Valid @RequestBody UserRequestDto userRequestDto) {
        return conversionService.convert(userService.create(userMapper.mapToUser(userRequestDto)), UserResponseDto.class);
    }

    @PutMapping
    public UserResponseDto update(@Valid @RequestBody UserRequestDto userRequestDto) {
        return conversionService.convert(userService.update(userMapper.mapToUser(userRequestDto)), UserResponseDto.class);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<UserResponseDto> getFriends(@PathVariable int id) {
        return userService.getFriends(id)
                .stream()
                .map(user -> conversionService.convert(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<UserResponseDto> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId)
                .stream()
                .map(user -> conversionService.convert(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/feed")
    public Collection<EventRequestDto> getFeed(@PathVariable int id) {
        return userService.getFeed(id)
                .stream()
                .map(event -> conversionService.convert(event, EventResponseDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/recommendations")
    public Collection<FilmResponseDto> getFilmRecommendations(@PathVariable int id) {
        return userService.getFilmRecommendations(id)
                .stream()
                .map(film -> conversionService.convert(film, FilmResponseDto.class))
                .collect(Collectors.toList());
    }
}
