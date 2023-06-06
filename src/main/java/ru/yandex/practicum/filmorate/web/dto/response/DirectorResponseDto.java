package ru.yandex.practicum.filmorate.web.dto.response;

import lombok.Data;

import javax.validation.constraints.NotBlank;
@Data
public class DirectorResponseDto {
    private Integer id;
    @NotBlank(message = "name can not be blank")
    private String name;
}
