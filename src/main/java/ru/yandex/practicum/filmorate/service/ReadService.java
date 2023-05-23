package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.storage.ReadStorage;

import java.util.List;

@Slf4j
public abstract class ReadService<T extends Entity> extends Service<T> {
    abstract ReadStorage<T> getReadStorage();

    public List<T> getAll() {
        log.trace("Getting all rows from {}", getServiceType());
        return this.getReadStorage().getAll();
    }

    public T getById(int id) {
        log.trace("Getting entity with type {}, id = {}", getServiceType(), id);
        return this.getReadStorage()
                .getById(id)
                .orElseThrow(() -> getNoDataFoundException(id));
    }

    protected void validateIds(int... entityIds) {
        for (int entityId : entityIds) {
            if (!this.getReadStorage().existsById(entityId)) {
                throw getNoDataFoundException(entityId);
            }
        }
    }
}
