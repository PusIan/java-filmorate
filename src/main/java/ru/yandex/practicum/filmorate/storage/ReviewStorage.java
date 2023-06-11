package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage extends Storage<Review> {

    List<Review> getReviewsByFilm(int filmId, int count);

    int addReviewLikeOrDislike(int reviewId, int userId, boolean isLike);

    int deleteReviewLikeOrDislike(int reviewId, int userId, boolean isLike);

    List<Review> getTopReviews(int count);

    boolean isReviewLiked(int reviewId, int userId, boolean isLike);
}
