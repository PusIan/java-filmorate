package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmSearchBy;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
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

    public Collection<Film> searchFilms(String query, String by) {
        List<FilmSearchBy> searchBySources = Arrays.stream(by.split(","))
                .distinct()
                .map(s -> {
                    try {
                        return FilmSearchBy.valueOf(s);
                    } catch (IllegalArgumentException e) {
                        throw new NotFoundException(String.format("Parameter %s is not supported", s));
                    }
                })
                .collect(Collectors.toList());

        return filmStorage.searchFilms(query, searchBySources);
    }

    @Override
    public void delete(int id) {
        this.filmStorage.delete(id);
    }
}
