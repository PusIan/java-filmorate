package ru.yandex.practicum.filmorate.web.validator.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.web.dto.request.UserRequestDto;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.web.validator.utils.UserValidatorTestUtils.getUserRequestDto;
import static ru.yandex.practicum.filmorate.web.validator.utils.ValidatorTestUtils.dtoHasErrorMessage;

class UserValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {" ", "213", "sfsdff@", "@sdfdsf"})
    public void createUserRequestDtoWrongEmailTest(String email) {
        UserRequestDto userRequestDto = getUserRequestDto(1, email, null,
                "name", LocalDate.now());
        Assertions.assertTrue(dtoHasErrorMessage(userRequestDto, "email must match pattern"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"12321@sdfdsf", "23213@sffs.ru"})
    public void createUserRequestDtoValidEmailTest(String email) {
        UserRequestDto userRequestDto = getUserRequestDto(1, email, null,
                "name", LocalDate.now());
        Assertions.assertFalse(dtoHasErrorMessage(userRequestDto, "email must match pattern"));
    }

    @Test
    public void createUserRequestDtoMandatoryFieldsTest() {
        UserRequestDto userRequestDtoNull = getUserRequestDto(null, null, null,
                null, null);
        Assertions.assertAll(
                () -> Assertions.assertTrue(dtoHasErrorMessage(userRequestDtoNull, "email can not be empty")),
                () -> Assertions.assertTrue(dtoHasErrorMessage(userRequestDtoNull, "login can not be empty"))
        );

        UserRequestDto userRequestDtoEmpty = getUserRequestDto(null, "", "",
                null, null);
        Assertions.assertAll(
                () -> Assertions.assertTrue(dtoHasErrorMessage(userRequestDtoEmpty, "email can not be empty")),
                () -> Assertions.assertTrue(dtoHasErrorMessage(userRequestDtoEmpty, "login can not be empty"))
        );
    }

    @Test
    public void createUserRequestDtoEmptyOrNullNameShouldBeLogin() {
        UserRequestDto userRequestDtoNull = getUserRequestDto(1, "123@123.ru", "login",
                null, LocalDate.now());

        Assertions.assertEquals("login", userRequestDtoNull.getName());

        UserRequestDto userRequestDtoEmpty = getUserRequestDto(1, "123@123.ru", "login",
                "", LocalDate.now());

        Assertions.assertEquals("login", userRequestDtoEmpty.getName());
    }

    @Test
    public void createUserWithFutureBirthdayError() {
        UserRequestDto userRequestDto = getUserRequestDto(1, "123@123.ru", "login",
                "name", LocalDate.now().plusDays(1));

        Assertions.assertTrue(dtoHasErrorMessage(userRequestDto, "Birthday can not be in future"));
    }
}