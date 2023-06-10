package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Entity;

@Slf4j
public abstract class CrudService<T extends Entity> extends WriteService<T> {
}
