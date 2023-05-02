package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Entity;

import java.util.*;

public abstract class InMemoryStorage<T extends Entity> implements Storage<T> {
    private final Map<Integer, T> entities = new HashMap<>();
    private int currentId = 1;

    @Override
    public T create(T entity) {
        entity.setId(currentId++);
        this.entities.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<T> update(T entity) {
        if (!entities.containsKey(entity.getId())) {
            return Optional.empty();
        }
        this.entities.put(entity.getId(), entity);
        return Optional.of(entity);
    }

    @Override
    public void delete(int id) {
        if (this.existsById(id)) {
            this.entities.remove(id);
        }
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public Optional<T> getById(int id) {
        if (!this.existsById(id)) {
            return Optional.empty();
        }
        return Optional.of(this.entities.get(id));
    }

    @Override
    public boolean existsById(int id) {
        return this.entities.containsKey(id);
    }
}
