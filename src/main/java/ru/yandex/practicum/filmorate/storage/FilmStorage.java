package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmSearchBy;

import java.util.List;

public interface FilmStorage extends Storage<Film> {
    List<Film> getPopularFilms(int count);

    void addLike(int userId, int filmId);

    void deleteLike(int userId, int filmId);

    List<Film> searchFilms(String query, List<FilmSearchBy> searchBySources);

    List<Film> getFilmsByIds(List<Integer> filmIds);

    List<Film> filmsDirectorSorted(int directorId, String sort);
}
