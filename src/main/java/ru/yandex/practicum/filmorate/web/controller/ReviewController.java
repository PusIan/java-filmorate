package ru.yandex.practicum.filmorate.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public Review create(@Valid @RequestBody Review review) {
        return reviewService.create(review);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        return reviewService.update(review);
    }

    @GetMapping("/{reviewId}")
    public Review getById(@PathVariable int reviewId) {
        return reviewService.getById(reviewId);
    }

    @DeleteMapping("/{reviewId}")
    public void deleteById(@PathVariable int reviewId) {
        reviewService.delete(reviewId);
    }

    @GetMapping
    public List<Review> getByFilmId(@RequestParam(defaultValue = "0") int filmId,
                                    @RequestParam(defaultValue = "10") int count) {
        return reviewService.getReviewByFilmOrAll(filmId, count);
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addReviewLike(@PathVariable int reviewId, @PathVariable int userId) {
        reviewService.addReviewLikeOrDislike(reviewId, userId, true);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addReviewDislike(@PathVariable int reviewId, @PathVariable int userId) {
        reviewService.addReviewLikeOrDislike(reviewId, userId, false);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void deleteReviewDislike(@PathVariable int reviewId, @PathVariable int userId) {
        reviewService.deleteReviewLikeOrDislike(reviewId, userId, false);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void deleteReviewLike(@PathVariable int reviewId, @PathVariable int userId) {
        reviewService.deleteReviewLikeOrDislike(reviewId, userId, true);
    }
}
