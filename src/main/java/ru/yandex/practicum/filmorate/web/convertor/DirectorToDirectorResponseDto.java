package ru.yandex.practicum.filmorate.web.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.web.dto.response.DirectorResponseDto;

@Component
public class DirectorToDirectorResponseDto implements Converter<Director, DirectorResponseDto> {
    @Override
    public DirectorResponseDto convert(Director source) {
        DirectorResponseDto directorResponseDto = new DirectorResponseDto();
        directorResponseDto.setId(source.getId());
        directorResponseDto.setName(source.getName());
        return directorResponseDto;
    }
}