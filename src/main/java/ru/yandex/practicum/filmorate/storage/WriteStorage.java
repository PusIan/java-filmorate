package ru.yandex.practicum.filmorate.storage;

import java.util.Optional;

public interface WriteStorage<T> {
    T create(T entity);

    Optional<T> update(T entity);

    void delete(int id);
}
