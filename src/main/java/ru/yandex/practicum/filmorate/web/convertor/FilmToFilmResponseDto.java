package ru.yandex.practicum.filmorate.web.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.web.dto.response.FilmResponseDto;
import ru.yandex.practicum.filmorate.web.dto.response.GenreResponseDto;
import ru.yandex.practicum.filmorate.web.dto.response.RatingMpaResponseDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class FilmToFilmResponseDto implements Converter<Film, FilmResponseDto> {
    @Override
    public FilmResponseDto convert(Film film) {
        FilmResponseDto filmResponseDto = new FilmResponseDto();
        filmResponseDto.setId(film.getId());
        filmResponseDto.setName(film.getName());
        filmResponseDto.setDescription(film.getDescription());
        filmResponseDto.setReleaseDate(film.getReleaseDate().toLocalDate());
        filmResponseDto.setDuration(film.getDuration());
        RatingMpaResponseDto ratingMpaResponseDto = new RatingMpaResponseDto();
        ratingMpaResponseDto.setId(film.getMpa().getId());
        ratingMpaResponseDto.setName(film.getMpa().getName());
        filmResponseDto.setMpa(ratingMpaResponseDto);
        List<GenreResponseDto> genreResponseDtos = new ArrayList<>();
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                GenreResponseDto genreResponseDto = new GenreResponseDto();
                genreResponseDto.setId(genre.getId());
                genreResponseDto.setName(genre.getName());
                genreResponseDtos.add(genreResponseDto);
            }
            filmResponseDto.setGenres(genreResponseDtos);
        }
        return filmResponseDto;
    }
}
