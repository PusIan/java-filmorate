package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage extends InMemoryStorage<Film> implements FilmStorage {
    @Override
    public List<Film> getPopularFilms(int count) {
        return this.getAll().stream()
                .sorted(Comparator.comparing(film -> -film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void addLike(int userId, int filmId) {
        if (this.existsById(filmId)) {
            this.getById(filmId).get().getLikes().add(userId);
        }
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        if (this.existsById(filmId)) {
            this.getById(filmId).get().getLikes().remove(userId);
        }
    }
}
