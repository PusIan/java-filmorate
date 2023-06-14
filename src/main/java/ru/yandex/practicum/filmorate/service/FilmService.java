package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmService extends CrudService<Film> {
    private final FilmStorage filmStorage;
    private final DirectorService directorService;
    private final UserService userService;

    public List<Film> filmsDirectorSorted(int directorId, DirectorSorted sort) {
        directorService.validateIds(directorId);
        return filmStorage.filmsDirectorSorted(directorId, sort);
    }

    public void addLike(int userId, int filmId) {
        this.userService.validateIds(userId);
        this.filmStorage.addLike(userId, filmId);
        userService.addEvent(userId, EventTypeFeed.LIKE, OperationFeed.ADD, filmId);
    }

    public void deleteLike(int userId, int filmId) {
        this.userService.validateIds(userId);
        this.filmStorage.deleteLike(userId, filmId);
        userService.addEvent(userId, EventTypeFeed.LIKE, OperationFeed.REMOVE, filmId);
    }

    public List<Film> getPopularFilms(int count, Optional<Integer> genreId, Optional<Integer> year) {
        return filmStorage.getPopularFilms(count, genreId, year);
    }

    public List<Film> getCommonFilms(int userId, int friendId) {
        return filmStorage.getCommonFilms(userId, friendId);
    }

    @Override
    Storage<Film> getStorage() {
        return this.filmStorage;
    }

    @Override
    String getServiceType() {
        return Film.class.getSimpleName();
    }

    public List<Film> searchFilms(String query, EnumSet<FilmSearchBy> filmSearchByList) {
        return filmStorage.searchFilms(query, filmSearchByList);
    }

    @Override
    public void delete(int id) {
        this.filmStorage.delete(id);
    }
}
