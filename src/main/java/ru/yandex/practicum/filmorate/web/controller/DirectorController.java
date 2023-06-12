package ru.yandex.practicum.filmorate.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.web.dto.request.DirectorRequestDto;
import ru.yandex.practicum.filmorate.web.dto.response.DirectorResponseDto;
import ru.yandex.practicum.filmorate.web.mapper.DirectorMapper;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/directors")
public class DirectorController {
    private final ConversionService conversionService;
    private final DirectorService directorService;
    private final DirectorMapper directorMapper;

    @GetMapping
    public Collection<DirectorResponseDto> getAll() {
        return directorService.getAll().stream()
                .map(director -> conversionService.convert(director, DirectorResponseDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public DirectorResponseDto getById(@PathVariable int id) {
        return conversionService.convert(directorService.getById(id), DirectorResponseDto.class);
    }

    @PostMapping
    public DirectorResponseDto create(@Valid @RequestBody DirectorRequestDto directorRequestDto) {
        return conversionService.convert(directorService.create(directorMapper.mapToDirector(directorRequestDto)), DirectorResponseDto.class);
    }

    @PutMapping
    public DirectorResponseDto update(@Valid @RequestBody DirectorRequestDto directorRequestDto) {
        return conversionService.convert(directorService.update(directorMapper.mapToDirector(directorRequestDto)), DirectorResponseDto.class);
    }

    @DeleteMapping("/{id}")
    public void removeById(@PathVariable int id) {
        directorService.delete(id);
    }
}
