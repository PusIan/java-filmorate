package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

@Service
public class FilmService extends CrudService<Film> {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }


    public void addLike(int userId, int filmId) {
        this.userService.validateIds(userId);
        this.filmStorage.addLike(userId, filmId);
    }

    public void deleteLike(int userId, int filmId) {
        this.userService.validateIds(userId);
        this.filmStorage.deleteLike(userId, filmId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    @Override
    Storage<Film> getStorage() {
        return this.filmStorage;
    }

    @Override
    String getServiceType() {
        return Film.class.getSimpleName();
    }
}
