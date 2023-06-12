package ru.yandex.practicum.filmorate.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.web.dto.request.FilmRequestDto;
import ru.yandex.practicum.filmorate.web.dto.response.FilmResponseDto;
import ru.yandex.practicum.filmorate.web.mapper.FilmMapper;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmController {
    private final ConversionService conversionService;
    private final FilmMapper filmMapper;
    private final FilmService filmService;

    @GetMapping
    public Collection<FilmResponseDto> getAll() {
        return filmService.getAll().stream()
                .map(film -> conversionService.convert(film, FilmResponseDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{filmId}")
    public FilmResponseDto getById(@PathVariable int filmId) {
        return conversionService.convert(filmService.getById(filmId), FilmResponseDto.class);
    }

    @PostMapping
    public FilmResponseDto create(@Valid @RequestBody FilmRequestDto filmRequestDto) {
        return conversionService.convert(filmService.create(filmMapper.mapToFilm(filmRequestDto)), FilmResponseDto.class);
    }

    @PutMapping
    public FilmResponseDto update(@Valid @RequestBody FilmRequestDto filmRequestDto) {
        return conversionService.convert(filmService.update(filmMapper.mapToFilm(filmRequestDto)), FilmResponseDto.class);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(userId, id);
    }

    @GetMapping("/popular")
    public Collection<FilmResponseDto> getPopularFilms(@RequestParam(defaultValue = "10") int count, @RequestParam Optional<Integer> genreId, @RequestParam Optional<Integer> year) {
        return this.filmService.getPopularFilms(count, genreId, year)
                .stream()
                .map(film -> conversionService.convert(film, FilmResponseDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public Collection<FilmResponseDto> searchFilms(@RequestParam String query, @RequestParam String by) {
        return this.filmService.searchFilms(query, by)
                .stream()
                .map(film -> conversionService.convert(film, FilmResponseDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/director/{directorId}")
    public Collection<FilmResponseDto> getFilmDirectorFromCountLikeOrYear(@PathVariable int directorId, @RequestParam(name = "sortBy") String sorted) {
        return this.filmService.filmsDirectorSorted(directorId, sorted)
                .stream()
                .map(film -> conversionService.convert(film, FilmResponseDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/common")
    public Collection<FilmResponseDto> getCommonFilms(@RequestParam(name = "userId") int userId,
                                     @RequestParam(name = "friendId") int friendId) {
        return this.filmService.getCommonFilms(userId, friendId)
                .stream()
                .map(film -> conversionService.convert(film, FilmResponseDto.class))
                .collect(Collectors.toList());
    }
}
