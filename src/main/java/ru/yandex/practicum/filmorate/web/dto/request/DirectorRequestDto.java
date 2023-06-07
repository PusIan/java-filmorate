package ru.yandex.practicum.filmorate.web.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DirectorRequestDto {
    private Integer id;
    @NotBlank(message = "name can not be blank")
    private String name;
}
