package ru.yandex.practicum.filmorate.web.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.DirectorSorted;

@Component
public class StringToDirectorSorted implements Converter<String, DirectorSorted> {
    @Override
    public DirectorSorted convert(String source) {
        return DirectorSorted.valueOf(source.toUpperCase());
    }
}
