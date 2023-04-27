package ru.yandex.practicum.filmorate.web.controller;

import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.web.dto.request.FilmRequestDto;
import ru.yandex.practicum.filmorate.web.dto.response.FilmResponseDto;
import ru.yandex.practicum.filmorate.web.mapper.FilmMapper;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private final ConversionService conversionService;
    private final FilmMapper filmMapper;
    private int currentId = 1;

    public FilmController(ConversionService conversionService, FilmMapper filmMapper) {
        this.conversionService = conversionService;
        this.filmMapper = filmMapper;
    }

    private int getCurrentId() {
        return currentId++;
    }

    @GetMapping
    public Collection<FilmResponseDto> findAll() {
        return films.values().stream()
                .map(film -> conversionService.convert(film, FilmResponseDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping
    public FilmResponseDto create(@Valid @RequestBody FilmRequestDto filmRequestDto) {
        int id = getCurrentId();
        filmRequestDto.setId(id);
        films.put(id, filmMapper.mapToFilm(filmRequestDto));
        return conversionService.convert(films.get(id), FilmResponseDto.class);
    }

    @PutMapping
    public FilmRequestDto update(@Valid @RequestBody FilmRequestDto filmRequestDto) {
        if (!films.containsKey(filmRequestDto.getId())) {
            throw new NotFoundException(String.format("Film with id %s not found", filmRequestDto.getId()));
        }
        films.put(filmRequestDto.getId(), filmMapper.mapToFilm(filmRequestDto));
        return conversionService.convert(films.get(filmRequestDto.getId()), FilmResponseDto.class);
    }
}
