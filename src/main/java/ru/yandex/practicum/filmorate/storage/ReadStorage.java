package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;

public interface ReadStorage<T> {
    List<T> getAll();

    Optional<T> getById(int id);

    boolean existsById(int id);
}
