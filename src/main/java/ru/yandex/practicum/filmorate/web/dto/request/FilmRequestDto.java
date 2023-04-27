package ru.yandex.practicum.filmorate.web.dto.request;

import lombok.Data;
import ru.yandex.practicum.filmorate.web.validator.FilmValid;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@FilmValid
public class FilmRequestDto {
    private Integer id;
    @NotBlank(message = "name can not be blank")
    private String name;
    @Size(max = 200, message = "max size for description is 200")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "duration must be positive integer")
    private Integer duration;
}
