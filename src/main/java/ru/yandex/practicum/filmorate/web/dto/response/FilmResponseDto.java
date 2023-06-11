package ru.yandex.practicum.filmorate.web.dto.response;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
public class FilmResponseDto {
    private Integer id;
    @NotBlank(message = "name can not be blank")
    private String name;
    @Size(max = 200, message = "max size for description is 200")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "duration must be positive integer")
    private Integer duration;
    private List<GenreResponseDto> genres;
    private RatingMpaResponseDto mpa;
    private List<DirectorResponseDto> directors;
}
