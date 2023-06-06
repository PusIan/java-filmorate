package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage extends Storage<Review> {

    List<Review> getReviewsByFilm(int filmId, int count);

    void addReviewLikeOrDislike(int reviewId, int userId, boolean isLike);

    void deleteReviewLikeOrDislike(int reviewId, int userId, boolean isLike);

    List<Review> getTop(int count);


}
