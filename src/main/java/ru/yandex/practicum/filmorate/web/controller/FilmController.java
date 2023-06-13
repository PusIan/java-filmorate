package ru.yandex.practicum.filmorate.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.DirectorSorted;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.web.convertor.FilmToFilmResponseDto;
import ru.yandex.practicum.filmorate.web.dto.request.FilmRequestDto;
import ru.yandex.practicum.filmorate.web.dto.response.FilmResponseDto;
import ru.yandex.practicum.filmorate.web.mapper.FilmMapper;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmController {
    private final FilmToFilmResponseDto convertToFilmRes;
    private final FilmMapper filmMapper;
    private final FilmService filmService;

    @GetMapping
    public Collection<FilmResponseDto> getAll() {
        return convertToFilmRes.getListResponse(filmService.getAll());
    }

    @GetMapping("/{filmId}")
    public FilmResponseDto getById(@PathVariable int filmId) {
        return convertToFilmRes.convert(filmService.getById(filmId));
    }

    @PostMapping
    public FilmResponseDto create(@Valid @RequestBody FilmRequestDto filmRequestDto) {
        return convertToFilmRes.convert(filmService.create(filmMapper.mapToFilm(filmRequestDto)));
    }

    @PutMapping
    public FilmResponseDto update(@Valid @RequestBody FilmRequestDto filmRequestDto) {
        return convertToFilmRes.convert(filmService.update(filmMapper.mapToFilm(filmRequestDto)));
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
        return convertToFilmRes.getListResponse(filmService.getPopularFilms(count, genreId, year));
    }

    @GetMapping("/search")
    public Collection<FilmResponseDto> searchFilms(@RequestParam String query, @RequestParam String by) {
        return convertToFilmRes.getListResponse(filmService.searchFilms(query, by));
    }

    @GetMapping("/director/{directorId}")
    public Collection<FilmResponseDto> getFilmDirectorFromCountLikeOrYear(@PathVariable int directorId, @RequestParam(name = "sortBy") DirectorSorted sorted) {
        return convertToFilmRes.getListResponse(filmService.filmsDirectorSorted(directorId, sorted));
    }

    @DeleteMapping("/{filmId}")
    public void delete(@PathVariable int filmId) {
        filmService.delete(filmId);
    }

    @GetMapping("/common")
    public Collection<FilmResponseDto> getCommonFilms(@RequestParam(name = "userId") int userId,
                                                      @RequestParam(name = "friendId") int friendId) {
        return convertToFilmRes.getListResponse(filmService.getCommonFilms(userId, friendId));
    }
}