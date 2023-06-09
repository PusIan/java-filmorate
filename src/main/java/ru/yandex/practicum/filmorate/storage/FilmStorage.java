package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage extends Storage<Film> {
    List<Film> getPopularFilms(int count, Optional<Integer> genreId, Optional<Integer> year);

    void addLike(int userId, int filmId);

    void deleteLike(int userId, int filmId);
}
