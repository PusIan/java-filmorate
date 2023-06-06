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

    @Override
    public Review create(Review entity) {
         checkFilmAndUser(entity);
         return super.create(entity);
    }

    @Override
    public Review update(Review entity) {
        int reviewId = entity.getReviewId();
        if (!reviewStorage.existsById(reviewId)) {
            throw new NotFoundException("Review with id " + reviewId + " not found.");
        }
        checkFilmAndUser(entity);
        return super.update(entity);
    }

    private void checkFilmAndUser(Review entity) {
        int filmId = entity.getFilmId();
        int userId = entity.getUserId();
        if (!filmStorage.existsById(filmId)) {
            throw new NotFoundException("Film with id " + filmId + " not found.");
        } else if (!userStorage.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found.");
        }
    }

    public void delete(int id) {
        log.trace("Delete entity with id={}.", id);
        if (!reviewStorage.existsById(id)) {
            throw new NotFoundException("Review with id " + id + " not found.");
        } else {
            reviewStorage.delete(id);
        }
    }

    public List<Review> getReviewByFilmOrAll(int filmId, int count) {
        if(filmId == 0) {
            return reviewStorage.getTopReviews(count);
        } else {
            return reviewStorage.getReviewsByFilm(filmId, count);
        }
    }

    public void addReviewLike(int reviewId, int userId) {
        if (...)
        reviewStorage.addReviewLikeOrDislike(reviewId, userId, true);
    }

    public void addReviewDislike(int reviewId, int userId) {
        reviewStorage.addReviewLikeOrDislike(reviewId, userId, false);
    }

    public void deleteReviewDislike(int reviewId, int userId) {
        reviewStorage.deleteReviewLikeOrDislike(reviewId, userId, false);
    }

    public void deleteReviewLike(int reviewId, int userId) {
        reviewStorage.deleteReviewLikeOrDislike(reviewId, userId, true);
    }
}
