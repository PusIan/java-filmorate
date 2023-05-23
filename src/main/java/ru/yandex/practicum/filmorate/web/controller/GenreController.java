package ru.yandex.practicum.filmorate.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.web.dto.response.GenreResponseDto;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreController {
    private final ConversionService conversionService;
    private final GenreService genreService;

    @GetMapping
    public Collection<GenreResponseDto> getAll() {
        return genreService.getAll().stream()
                .map(genre -> conversionService.convert(genre, GenreResponseDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{genreId}")
    public GenreResponseDto getById(@PathVariable int genreId) {
        return conversionService.convert(genreService.getById(genreId), GenreResponseDto.class);
    }
}