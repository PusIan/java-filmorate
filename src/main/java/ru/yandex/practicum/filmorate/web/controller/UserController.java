package ru.yandex.practicum.filmorate.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.web.convertor.FilmToFilmResponseDto;
import ru.yandex.practicum.filmorate.web.convertor.UserToUserResponseDto;
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
    private final UserToUserResponseDto convertToUserRes;
    private final FilmToFilmResponseDto convertToFilmRes;
    private final ConversionService conversionService;
    private final UserMapper userMapper;
    private final UserService userService;


    @GetMapping
    public Collection<UserResponseDto> getAll() {
        return convertToUserRes.getListResponse(userService.getAll());
    }

    @GetMapping("/{userId}")
    public UserResponseDto getById(@PathVariable int userId) {
        return convertToUserRes.convert(userService.getById(userId));
    }

    @PostMapping
    public UserResponseDto create(@Valid @RequestBody UserRequestDto userRequestDto) {
        return convertToUserRes.convert(userService.create(userMapper.mapToUser(userRequestDto)));
    }

    @PutMapping
    public UserResponseDto update(@Valid @RequestBody UserRequestDto userRequestDto) {
        return convertToUserRes.convert(userService.update(userMapper.mapToUser(userRequestDto)));
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
        return convertToUserRes.getListResponse(userService.getFriends(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<UserResponseDto> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return convertToUserRes.getListResponse(userService.getCommonFriends(id, otherId));
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
        return convertToFilmRes.getListResponse(userService.getFilmRecommendations(id));
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable int userId) {
        userService.delete(userId);
    }
}
