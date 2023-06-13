package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.DirectorSorted;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmSearchBy;

import java.util.List;
import java.util.Optional;

public interface FilmStorage extends Storage<Film> {
    List<Film> getPopularFilms(int count, Optional<Integer> genreId, Optional<Integer> year);

    void addLike(int userId, int filmId);

    void deleteLike(int userId, int filmId);

    List<Film> searchFilms(String query, List<FilmSearchBy> searchBySources);

    List<Film> getFilmsByIds(List<Integer> filmIds);

    List<Film> filmsDirectorSorted(int directorId, DirectorSorted sort);

    List<Film> getCommonFilms(int userId, int friendId);
}
