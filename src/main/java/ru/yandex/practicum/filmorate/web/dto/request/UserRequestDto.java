package ru.yandex.practicum.filmorate.web.dto.request;

import lombok.Data;
import ru.yandex.practicum.filmorate.web.validator.UserValid;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@UserValid
public class UserRequestDto {
    private Integer id;
    @NotEmpty(message = "email can not be empty")
    @Email(message = "email must match pattern")
    private String email;
    @NotEmpty(message = "login can not be empty")
    @NotBlank(message = "login can not be blank")
    private String login;
    private String name;
    private LocalDate birthday;

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            this.name = this.login;
        } else {
            this.name = name;
        }
    }
}