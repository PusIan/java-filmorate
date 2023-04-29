package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

@Slf4j
public abstract class CrudService<T extends Entity> {

    private final String notFoundExceptionMessage = "%s with id %s not found";

    abstract Storage<T> getStorage();

    abstract String getServiceType();

    NotFoundException getNoDataFoundException(int id) {
        return new NotFoundException(String.format(this.notFoundExceptionMessage, getServiceType(), id));
    }

    NotFoundException getNoDataFoundException(int id, String serviceType) {
        return new NotFoundException(String.format(this.notFoundExceptionMessage, serviceType, id));
    }

    public List<T> getAll() {
        log.trace("Getting all rows from {}", getServiceType());
        return this.getStorage().getAll();
    }

    public T create(T entity) {
        log.trace("Create entity {}", entity.toString());
        return this.getStorage().create(entity);
    }

    public T update(T entity) {
        log.trace("Update entity {}", entity.toString());
        return this.getStorage()
                .update(entity)
                .orElseThrow(() -> getNoDataFoundException(entity.getId()));
    }

    public T getById(int id) {
        log.trace("Getting entity with type {}, id = {}", getServiceType(), id);
        return this.getStorage()
                .getById(id)
                .orElseThrow(() -> getNoDataFoundException(id));
    }
}
