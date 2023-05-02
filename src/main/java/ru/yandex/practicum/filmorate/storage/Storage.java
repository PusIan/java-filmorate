package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<T> {
    T create(T entity);

    Optional<T> update(T entity);

    void delete(int id);

    List<T> getAll();

    Optional<T> getById(int id);

    boolean existsById(int id);
}
