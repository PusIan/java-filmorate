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
        String selectAll = "SELECT review_id, user_id, film_id, is_positive, "
        + "useful, content FROM reviews ORDER BY useful DESC";
        return jdbcTemplate.query(selectAll, new BeanPropertyRowMapper<>(Review.class));
    }

    public List<Review> getTopReviews(int count) {
        String selectAll = "SELECT review_id, user_id, film_id, is_positive, " +
                           "useful, content FROM reviews ORDER BY useful DESC limit ?";
        return jdbcTemplate.query(selectAll, new BeanPropertyRowMapper<>(Review.class), count);
    }

    @Override
    public Optional<Review> getById(int id) {
        String queryById = "SELECT review_id, user_id, film_id, is_positive, " +
                           "useful, content FROM reviews WHERE review_id=?";
        return jdbcTemplate.query(queryById, new BeanPropertyRowMapper<>(Review.class), id)
                .stream()
                .findFirst();
    }

    @Override
    public boolean existsById(int id) {
        String find = "SELECT (EXISTS (SELECT 1 FROM reviews WHERE review_id=?))";
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
            PreparedStatement psReview = connection.prepareStatement(insertReview, new String[]{"review_id"});
            psReview.setInt(1, review.getUserId());
            psReview.setInt(2, review.getFilmId());
            psReview.setBoolean(3, review.isIsPositive());
            psReview.setInt(4, review.getUseful());
            psReview.setString(5, review.getContent());
            return psReview;
        }, keyHolder);
        int reviewId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return getById(reviewId).orElseThrow();
    }

    /**
     * Исключен апдейт user_id, film_id (из-за тестов).
     */
    @Override
    public Optional<Review> update(Review review) {
        int reviewId = review.getId();
        String updReview = "UPDATE reviews SET is_positive=?, content=? "
                         + "WHERE review_id=?";
        jdbcTemplate.update(updReview,
                review.isIsPositive(),
                review.getContent(),
                reviewId
        );
        return getById(reviewId);
    }

    /**
     * Сначала удаляем лайки у отзыва.
     * На случай без дилит каскада.
     */
    @Override
    public void delete(int reviewId) {
        String deleteLikes = "DELETE FROM review_likes WHERE review_id=?";
        String delReview = "DELETE FROM reviews WHERE review_id=?";
        jdbcTemplate.update(deleteLikes, reviewId);
        jdbcTemplate.update(delReview, reviewId);
    }

    @Override
    public List<Review> getReviewsByFilm(int filmId, int count) {
        String selectByFilm = "SELECT review_id, user_id, film_id, is_positive, " +
                "useful, content FROM reviews WHERE film_id=? ORDER BY useful DESC limit ?";
        return jdbcTemplate.query(selectByFilm, new BeanPropertyRowMapper<>(Review.class), filmId, count);
    }

    /**
     * При лайке/дизлайке накручиваем/скручиваем useful у отзыва.
     */
    @Override
    public int addReviewLikeOrDislike(int reviewId, int userId, boolean isLike) {
        String insertLike = "INSERT INTO review_likes (user_id, review_id, is_like) VALUES (?, ?, ?)";
        return updateUsefulAfterUpdate(insertLike, userId, reviewId, isLike);
    }

    /**
     * Запрос для лайка и обновление useful.
     * @param query     удаление или вставка
     * @return           записей изменено
     */
    private int updateUsefulAfterUpdate(String query, int userId, int reviewId, boolean isLike) {
        String plusMinus = isLike ? "+1" : "-1";
        String updUseful = "UPDATE reviews SET useful = useful" + plusMinus + " WHERE review_id=?";
        int returnCount = jdbcTemplate.update(query, userId, reviewId, isLike);
        if (returnCount != 0) {
            jdbcTemplate.update(updUseful, reviewId);
        }
        return returnCount;
    }

    /**
     * При удалении лайка/дизлайка скручиваем/накручиваем useful у отзыва.
     */
    @Override
    public int deleteReviewLikeOrDislike(int reviewId, int userId, boolean isLike) {
        String deleteLike = "DELETE FROM review_likes WHERE user_id=? AND review_id=? AND is_like=?";
        return updateUsefulAfterUpdate(deleteLike, userId, reviewId, isLike);
    }
}
