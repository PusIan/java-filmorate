package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.storage.ReadStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

@Slf4j
public abstract class WriteService<T extends Entity> extends ReadService<T> {
    abstract Storage<T> getStorage();

    @Override
    protected ReadStorage<T> getReadStorage() {
        return this.getStorage();
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
}
