package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmService extends CrudService<Film> {
    private final FilmStorage filmStorage;
    private final DirectorService directorService;
    private final UserService userService;

    public List<Film> filmsDirectorSorted(int directorId, String sort) {
        directorService.validateIds(directorId);
        return filmStorage.filmsDirectorSorted(directorId, sort);
    }

    public void addLike(int userId, int filmId) {
        this.userService.validateIds(userId);
        this.filmStorage.addLike(userId, filmId);
    }

    public void deleteLike(int userId, int filmId) {
        this.userService.validateIds(userId);
        this.filmStorage.deleteLike(userId, filmId);
    }

    public List<Film> getPopularFilms(int count, Optional<Integer> genreId, Optional<Integer> year) {
        return filmStorage.getPopularFilms(count, genreId, year);
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
