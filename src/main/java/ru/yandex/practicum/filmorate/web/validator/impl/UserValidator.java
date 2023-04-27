package ru.yandex.practicum.filmorate.web.validator.impl;

import ru.yandex.practicum.filmorate.web.dto.request.UserRequestDto;
import ru.yandex.practicum.filmorate.web.validator.UserValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class UserValidator implements ConstraintValidator<UserValid, UserRequestDto> {
    @Override
    public boolean isValid(UserRequestDto userRequestDto, ConstraintValidatorContext context) {
        if (userRequestDto.getBirthday() != null && userRequestDto.getBirthday().isAfter(LocalDate.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Birthday can not be in future")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
