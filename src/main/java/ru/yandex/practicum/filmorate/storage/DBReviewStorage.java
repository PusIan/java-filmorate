package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DBReviewStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Review> getAll() {
        String selectAll = "SELECT * FROM reviews ORDER BY useful DESC";
        return jdbcTemplate.query(selectAll, new BeanPropertyRowMapper<>(Review.class));
    }

    public List<Review> getTop(int count) {
        String selectAll = "SELECT * FROM reviews ORDER BY useful DESC limit ?";
        return jdbcTemplate.query(selectAll, new BeanPropertyRowMapper<>(Review.class), count);
    }

    @Override
    public Optional<Review> getById(int id) {
        String queryById = "SELECT * FROM reviews WHERE id=?";
        return jdbcTemplate.query(queryById, new BeanPropertyRowMapper<>(Review.class), id)
                .stream()
                .findFirst();
    }

    @Override
    public boolean existsById(int id) {
        String find = "SELECT (EXISTS (SELECT 1 FROM reviews WHERE id=?))";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(find, id);
        srs.next();
        return srs.getBoolean(1);
    }

    @Override
    public Review create(Review review) {
        String insertReview = "INSERT INTO reviews (user_id, film_id, is_positive, useful, content) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement psReview = connection.prepareStatement(insertReview, new String[]{"id"});
            psReview.setInt(1, review.getUserId());
            psReview.setInt(2, review.getFilmId());
            psReview.setBoolean(3, review.isPositive());
            psReview.setInt(4, review.getUseful());
            psReview.setString(5, review.getContent());
            return psReview;
        }, keyHolder);
        int reviewId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return getById(reviewId).orElseThrow();
    }

    @Override
    public Optional<Review> update(Review review) {
        int reviewId = review.getReviewId();
        String updReview = "UPDATE reviews SET user_id=?, film_id=?, is_positive=?, "
                         + "useful=?, content=? WHERE id=?";
        jdbcTemplate.update(updReview
                , review.getUserId()
                , review.getFilmId()
                , review.isPositive()
                , review.getUseful()
                , review.getContent()
                , reviewId
        );
        return getById(reviewId);
    }

    @Override
    public void delete(int id) {
        String delReview = "DELETE FROM reviews WHERE id=?";
        jdbcTemplate.update(delReview, id);
    }

    @Override
    public List<Review> getReviewsByFilm(int filmId, int count) {
        String selectByFilm = "SELECT * FROM reviews WHERE film_id=? ORDER BY useful DESC limit ?";
        return jdbcTemplate.query(selectByFilm, new BeanPropertyRowMapper<>(Review.class), filmId, count);
    }

    /**
     * При лайке/дизлайке накручиваем/скручиваем useful у отзыва.
     */
    @Override
    public void addReviewLikeOrDislike(int userId, int reviewId, boolean isLike) {
        String insertLike = "INSERT INTO review_likes (user_id, review_id, is_like) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertLike, userId, reviewId, isLike);
        updateUseful(reviewId, isLike);
    }
    /**
     * При удалении лайка/дизлайка скручиваем/накручиваем useful у отзыва.
     */
    @Override
    public void deleteReviewLikeOrDislike(int userId, int reviewId, boolean isLike) {
        String deleteLike = "DELETE FROM review_likes WHERE user_id=? AND review_id=? AND is_like=?";
        jdbcTemplate.update(deleteLike, userId, reviewId, isLike);
        updateUseful(reviewId, !isLike);
    }

    private void updateUseful(int reviewId, boolean isLike) {
        String plusMinus = isLike ? "+ 1" : "- 1";
        String updReview = "UPDATE review SET useful = useful + " + plusMinus + " WHERE id=?";
        jdbcTemplate.update(updReview, reviewId);
    }
}