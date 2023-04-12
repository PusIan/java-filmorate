package ru.yandex.practicum.filmorate.web.validator.utils;

import ru.yandex.practicum.filmorate.web.dto.request.FilmRequestDto;

import java.time.LocalDate;

public class FilmValidatorTestUtils {
    public static FilmRequestDto getFilmRequestDto(Integer id, String name, String description,
                                                   LocalDate releaseDate, Integer duration) {
        FilmRequestDto filmRequestDto = new FilmRequestDto();
        filmRequestDto.setId(id);
        filmRequestDto.setName(name);
        filmRequestDto.setDescription(description);
        filmRequestDto.setReleaseDate(releaseDate);
        filmRequestDto.setDuration(duration);
        return filmRequestDto;
    }
}