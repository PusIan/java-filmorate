package ru.yandex.practicum.filmorate.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.web.dto.request.ReviewRequestDto;
import ru.yandex.practicum.filmorate.web.dto.response.ReviewResponseDto;
import ru.yandex.practicum.filmorate.web.mapper.ReviewMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReviewController {
    private final ConversionService conversionService;
    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @PostMapping
    public ReviewResponseDto create(@Valid @RequestBody ReviewRequestDto reviewRequestDto) {
        return conversionService.convert(reviewService.create(
                reviewMapper.mapToReview(reviewRequestDto)), ReviewResponseDto.class);
    }

    @PutMapping
    public ReviewResponseDto update(@Valid @RequestBody ReviewRequestDto reviewRequestDto) {
        return conversionService.convert(reviewService.update(
                reviewMapper.mapToReview(reviewRequestDto)), ReviewResponseDto.class);
    }

    @GetMapping("/{reviewId}")
    public ReviewResponseDto getById(@PathVariable int reviewId) {
        return conversionService.convert(reviewService.getById(reviewId), ReviewResponseDto.class);
    }

    @DeleteMapping("/{reviewId}")
    public void deleteById(@PathVariable int reviewId) {
        reviewService.delete(reviewId);
    }

    @GetMapping
    public List<ReviewResponseDto> getByFilmId(@RequestParam(defaultValue = "0") int filmId,
                                               @RequestParam(defaultValue = "10") int count) {
        return reviewService.getReviewByFilmOrAll(filmId, count).stream().map(
                review -> conversionService.convert(review, ReviewResponseDto.class)
        ).collect(Collectors.toList());
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
