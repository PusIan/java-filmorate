package ru.yandex.practicum.filmorate.web.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmSearchBy;

@Component
public class StringToFilmSearchBy implements Converter<String, FilmSearchBy> {
    @Override
    public FilmSearchBy convert(String source) {
        return FilmSearchBy.valueOf(source.toUpperCase());
    }
}
