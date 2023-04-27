package ru.yandex.practicum.filmorate.web.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.web.dto.response.FilmResponseDto;

@Component
public class FilmToFilmResponseDto implements Converter<Film, FilmResponseDto> {
    @Override
    public FilmResponseDto convert(Film film) {
        FilmResponseDto filmResponseDto = new FilmResponseDto();
        filmResponseDto.setId(film.getId());
        filmResponseDto.setName(film.getName());
        filmResponseDto.setDescription(film.getDescription());
        filmResponseDto.setReleaseDate(film.getReleaseDate());
        filmResponseDto.setDuration(film.getDuration());
        return filmResponseDto;
    }
}
