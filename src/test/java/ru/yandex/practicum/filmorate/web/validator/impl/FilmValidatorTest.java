package ru.yandex.practicum.filmorate.web.validator.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.web.dto.request.FilmRequestDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static ru.yandex.practicum.filmorate.web.validator.utils.FilmValidatorTestUtils.getFilmRequestDto;
import static ru.yandex.practicum.filmorate.web.validator.utils.ValidatorTestUtils.dtoHasErrorMessage;

public class FilmValidatorTest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Test
    public void createFilmRequestDtoMandatoryFieldsTest() {
        FilmRequestDto filmRequestDtoNull = getFilmRequestDto(null, null, null,
                null, null);
        Assertions.assertTrue(dtoHasErrorMessage(filmRequestDtoNull, "name can not be blank"));
    }

    @Test
    public void createFilmRequestDtoDescriptionSizeTest() {
        String escriptionSize200 = new String(new char[200]).replace('\0', '1');
        FilmRequestDto filmRequestDtoCorrect = getFilmRequestDto(1, "name", escriptionSize200,
                null, null);
        Assertions.assertFalse(dtoHasErrorMessage(filmRequestDtoCorrect, "max size for description is 200"));

        String descriptionSize201 = new String(new char[201]).replace('\0', '1');
        FilmRequestDto filmRequestDtoWrong = getFilmRequestDto(1, "name", descriptionSize201,
                null, null);
        Assertions.assertTrue(dtoHasErrorMessage(filmRequestDtoWrong, "max size for description is 200"));
    }

    @Test
    public void createFilmRequestDtoReleaseDateTest() {
        LocalDate boundaryDate = LocalDate.of(1895, 12, 28);
        FilmRequestDto filmRequestDtoWrongDate = getFilmRequestDto(1, "name", "description",
                boundaryDate.minusDays(1), null);
        Assertions.assertTrue(dtoHasErrorMessage(filmRequestDtoWrongDate,
                "Film release min date is " + boundaryDate.format(formatter)));

        FilmRequestDto filmRequestDtoBoundaryDate = getFilmRequestDto(1, "name", "description",
                boundaryDate, null);
        Assertions.assertFalse(dtoHasErrorMessage(filmRequestDtoBoundaryDate,
                "Film release min date is " + boundaryDate.format(formatter)));

        FilmRequestDto filmRequestDtoCorrectDate = getFilmRequestDto(1, "name", "description",
                boundaryDate.plusDays(1), null);
        Assertions.assertFalse(dtoHasErrorMessage(filmRequestDtoBoundaryDate,
                "Film release min date is " + boundaryDate.format(formatter)));
    }

    @Test
    public void createFilmRequestDtoDurationMustBePositive() {
        FilmRequestDto filmRequestDtoNegativeDuration = getFilmRequestDto(1, "name", "description",
                LocalDate.now(), -1);
        Assertions.assertTrue(dtoHasErrorMessage(filmRequestDtoNegativeDuration,
                "duration must be positive integer"));

        FilmRequestDto filmRequestDtoPositiveDuration = getFilmRequestDto(1, "name", "description",
                LocalDate.now(), 100);
        Assertions.assertFalse(dtoHasErrorMessage(filmRequestDtoPositiveDuration,
                "duration must be positive integer"));
    }
}
