package ru.yandex.practicum.filmorate.web.validator.impl;

import ru.yandex.practicum.filmorate.web.dto.request.FilmRequestDto;
import ru.yandex.practicum.filmorate.web.validator.FilmValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FilmValidator implements ConstraintValidator<FilmValid, FilmRequestDto> {
    private static final LocalDate FILM_RELEASE_MIN_DATE = LocalDate.of(1895, 12, 28);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public boolean isValid(FilmRequestDto filmRequestDto, ConstraintValidatorContext context) {
        if (filmRequestDto.getReleaseDate() != null && filmRequestDto.getReleaseDate().isBefore(FILM_RELEASE_MIN_DATE)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.format("Film release min date is %s",
                            FILM_RELEASE_MIN_DATE.format(formatter)))
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
