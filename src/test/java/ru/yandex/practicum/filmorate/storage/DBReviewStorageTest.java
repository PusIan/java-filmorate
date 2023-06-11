package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ru.yandex.practicum.filmorate.web.starter.FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DBReviewStorageTest {
    private final ReviewStorage reviewStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Test
    public void shouldCreateUpdateDeleteReview() {
        Review review = setInputReview();
        Review createdReview = reviewStorage.create(review);
        assertEquals(review, createdReview);
        boolean newPositive = !createdReview.getIsPositive();
        createdReview.setIsPositive(newPositive);
        final Review updatedReview = reviewStorage.update(createdReview).orElse(null);
        assertNotNull(updatedReview);
        int reviewId = updatedReview.getReviewId();
        reviewStorage.delete(reviewId);
        assertAll(
                () -> assertEquals(updatedReview.getIsPositive(), newPositive),
                () -> assertTrue(reviewStorage.getById(reviewId).isEmpty())
        );
    }

    @Test
    public void shouldDelLikeAfterLike() {
        Review review = reviewStorage.create(setInputReview());
        User user = userStorage.create(Fixtures.getUser2());
        reviewStorage.addReviewLikeOrDislike(review.getReviewId(), user.getId(), true);
        assertTrue(reviewStorage.isReviewLiked(review.getReviewId(), user.getId(), true));
        reviewStorage.deleteReviewLikeOrDislike(review.getReviewId(), user.getId(), true);
        assertFalse(reviewStorage.isReviewLiked(review.getReviewId(), user.getId(), true));
    }

    private Review setInputReview() {
        User createdUser = userStorage.create(Fixtures.getUser1());
        Film createdFilm = filmStorage.create(Fixtures.getFilm1());
        return Fixtures.getReview(createdUser.getId(), createdFilm.getId());
    }
}