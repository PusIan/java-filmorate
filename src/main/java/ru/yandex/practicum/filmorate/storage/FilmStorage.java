package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage extends Storage<Film> {
    List<Film> getPopularFilms(int count);

    void addLike(int userId, int filmId);

    void deleteLike(int userId, int filmId);

    List<Film> filmsDirectorSorted(int directorId, String sort);
}
