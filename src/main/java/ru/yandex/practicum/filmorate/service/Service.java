package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Entity;

public abstract class Service<T extends Entity> {
    private final String notFoundExceptionMessage = "%s with id %s not found";

    abstract String getServiceType();

    NotFoundException getNoDataFoundException(int id) {
        return new NotFoundException(String.format(this.notFoundExceptionMessage, getServiceType(), id));
    }

    NotFoundException getNoDataFoundException(int id, String serviceType) {
        return new NotFoundException(String.format(this.notFoundExceptionMessage, serviceType, id));
    }
}
