package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService extends CrudService<Film> {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }


    public void addLike(int userId, int filmId) {
        this.userStorage.validateId(userId)
                .orElseThrow(() -> getNoDataFoundException(userId, User.class.getSimpleName()));
        this.filmStorage.getById(filmId)
                .orElseThrow(() -> getNoDataFoundException(filmId))
                .getLikes()
                .add(userId);
    }

    public void deleteLike(int userId, int filmId) {
        this.userStorage.validateId(userId)
                .orElseThrow(() -> getNoDataFoundException(userId, User.class.getSimpleName()));
        this.filmStorage.getById(filmId)
                .orElseThrow(() -> getNoDataFoundException(filmId))
                .getLikes()
                .remove(userId);
    }

    public List<Film> getPopularFilms(int count) {
        return this.getAll().stream()
                .sorted(Comparator.comparing(film -> -film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
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
