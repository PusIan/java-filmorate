package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReviewService extends CrudService<Review> {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final ReviewStorage reviewStorage;


    @Override
    String getServiceType() {
        return Review.class.getSimpleName();
    }

    @Override
    Storage<Review> getStorage() {
        return reviewStorage;
    }

    public void delete(int id) {
        log.trace("Delete entity with id={}.", id);
        if (!reviewStorage.existsById(id)) {
            throw new NotFoundException("Entity with id " + id + " not found.");
        } else {
            reviewStorage.delete(id);
        }
    }

    public List<Review> getReviewByFilmOrAll(int filmId, int count) {
        if(filmId == 0) {
            return reviewStorage.getTop(count);
        } else {
            return reviewStorage.getReviewsByFilm(filmId, count);
        }
    }
}
