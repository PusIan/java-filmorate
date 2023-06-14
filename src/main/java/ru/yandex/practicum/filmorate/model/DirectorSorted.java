package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DirectorSorted {
    YEAR("year"),
    LIKES("likes");

    private final String sort;
}
