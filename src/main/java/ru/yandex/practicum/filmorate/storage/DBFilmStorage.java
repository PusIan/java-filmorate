package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DBFilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RatingMpaStorage ratingMpaStorage;
    private final GenreStorage genreStorage;

    @Override
    public List<Film> getPopularFilms(int count, Optional<Integer> genreId, Optional<Integer> year) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        String sqlQuery = "SELECT f.* FROM film f\n" +
                "LEFT JOIN like_ l ON l.film_id = f.id\n" +
                "LEFT JOIN film_genre fg ON fg.film_id = f.id\n" +
                "WHERE 1=1\n";
        if (genreId.isPresent()) {
            sqlQuery += "AND fg.genre_id = :genreId\n";
            mapSqlParameterSource.addValue("genreId", genreId.get());
        }
        if (year.isPresent()) {
            sqlQuery += "AND TO_CHAR(f.release_date,'yyyy') = :releaseDate\n";
            mapSqlParameterSource.addValue("releaseDate", year.get());
        }
        mapSqlParameterSource.addValue("rowLimit", count);
        sqlQuery += "GROUP BY f.id\n" +
                "ORDER BY COUNT(l.id) DESC\n" +
                "LIMIT :rowLimit";
        return namedParameterJdbcTemplate.query(sqlQuery, mapSqlParameterSource, this::mapRowToFilm);
    }

    @Override
    public void addLike(int userId, int filmId) {
        String sqlQueryForLikes = "SELECT user_id FROM like_ WHERE film_id = ?";
        List<Integer> likes = jdbcTemplate.queryForList(sqlQueryForLikes, Integer.class, filmId);
        if (!likes.contains(userId)) {
            String sqlQuery = "INSERT INTO like_ (film_id, user_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlQuery, filmId, userId);
        }
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        String sqlQuery = "DELETE FROM like_ WHERE film_id=? AND user_id=?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> searchFilms(String query, EnumSet<FilmSearchBy> filmSearchByList) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(Map.of(
                "query", "%" + query + "%"));
        String sqlQuery = "SELECT f.* FROM film f\n" +
                "LEFT JOIN like_ l ON l.film_id = f.id\n" +
                "LEFT JOIN film_directors fd on fd.film_id = f.id\n" +
                "LEFT JOIN director d on d.id = fd.director_id\n" +
                "WHERE 0=1\n";
        for (FilmSearchBy filmSearchBy : filmSearchByList) {
            switch (filmSearchBy) {
                case TITLE:
                    sqlQuery += "OR LOWER(f.name) like LOWER(:query)\n";
                    break;
                case DIRECTOR:
                    sqlQuery += "OR LOWER(d.name) like LOWER(:query)\n";
                    break;
                default:
                    throw new RuntimeException(filmSearchBy + " not supported");
            }
        }
        sqlQuery += "GROUP BY f.id\n" +
                "ORDER BY COUNT(l.id) DESC\n";
        return namedParameterJdbcTemplate.query(sqlQuery, sqlParameterSource, this::mapRowToFilm);
    }

    @Override
    public List<Film> getFilmsByIds(List<Integer> filmIds) {
        SqlParameterSource parameters = new MapSqlParameterSource("filmIds", filmIds);
        String sqlQuery = "SELECT * FROM film WHERE id IN (:filmIds)";
        return namedParameterJdbcTemplate.query(
                sqlQuery,
                parameters,
                this::mapRowToFilm
        );
    }

    @Override
    public List<Film> filmsDirectorSorted(int directorId, DirectorSorted sort) {
        if (sort.equals(DirectorSorted.YEAR)) {
            String sqlQuery = "SELECT f.* FROM film f\n" +
                    "where f.ID in (select film_id from film_directors where director_id = ?)" +
                    "GROUP BY f.id\n" +
                    "ORDER BY f.id DESC\n";
            return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, directorId);
        } else if (sort.equals(DirectorSorted.LIKES)) {
            String sqlQuery = "SELECT f.* FROM film f\n" +
                    "LEFT JOIN like_ l ON l.film_id = f.id\n" +
                    "where f.ID in (SELECT film_id FROM film_directors WHERE director_id = ?)" +
                    "GROUP BY f.id\n" +
                    "ORDER BY COUNT(l.id) DESC\n";
            return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, directorId);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public Film create(Film entity) {
        String sqlQueryFilm = "INSERT INTO film(name, description, release_date, duration, rating_mpa_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmtFilm = connection.prepareStatement(sqlQueryFilm, new String[]{"id"});
            stmtFilm.setString(1, entity.getName());
            stmtFilm.setString(2, entity.getDescription());
            stmtFilm.setDate(3, entity.getReleaseDate());
            stmtFilm.setInt(4, entity.getDuration());
            stmtFilm.setObject(5, entity.getMpa().getId(), Types.INTEGER);
            return stmtFilm;
        }, keyHolder);
        int filmId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        this.saveFilmDirectors(filmId, entity.getDirectors());
        this.saveFilmGenre(filmId, entity.getGenres());
        return this.getById(filmId).orElseThrow();
    }

    @Override
    @Transactional
    public Optional<Film> update(Film entity) {
        String sqlQuery = "UPDATE film SET name=?, description=?, release_date=?, duration=?, rating_mpa_id=?" +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery, entity.getName(), entity.getDescription(), entity.getReleaseDate(),
                entity.getDuration(), entity.getMpa().getId(), entity.getId());
        this.saveFilmDirectors(entity.getId(), entity.getDirectors());
        this.saveFilmGenre(entity.getId(), entity.getGenres());
        return this.getById(entity.getId());
    }

    @Override
    public void delete(int filmId) {
        String sqlQuery = "DELETE FROM film WHERE id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT * FROM film";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Optional<Film> getById(int id) {
        String sqlQuery = "SELECT * FROM film WHERE id = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, id)
                .stream()
                .findFirst();
    }

    private void saveFilmDirectors(Integer filmId, List<Director> directorList) {
        if (directorList != null && !directorList.isEmpty()) {
            String sqlQueryDeleteDirectorsForNotExistentIds = "DELETE FROM film_directors WHERE film_id = :filmId " +
                    "AND director_id NOT IN (:directorId)";
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource(Map.of(
                    "filmId", filmId,
                    "directorId", directorList.stream().map(Director::getId).toArray()));
            namedParameterJdbcTemplate.update(sqlQueryDeleteDirectorsForNotExistentIds, sqlParameterSource);
            String sqlQueryMergeGenre = "MERGE INTO film_directors (film_id, director_id) KEY (film_id, director_id) " +
                    "SELECT ?, ? FROM dual";
            jdbcTemplate.batchUpdate(sqlQueryMergeGenre,
                    directorList,
                    100,
                    (preparedStatement, directors) -> {
                        preparedStatement.setInt(1, filmId);
                        preparedStatement.setInt(2, directors.getId());
                    });
        } else {
            String sqlQueryDeleteAllGenre = "DELETE FROM film_directors WHERE film_id = ?";
            jdbcTemplate.update(sqlQueryDeleteAllGenre, filmId);
        }
    }

    @Override
    public List<Film> getCommonFilms(int userId, int friendId) {
        String sql = "SELECT f.* " +
                "FROM like_ l1 " +
                "INNER JOIN like_ l2 ON l2.film_id = l1.film_id " +
                "INNER JOIN film f ON l1.film_id = f.id " +
                "INNER JOIN like_ total_film_likes ON total_film_likes.film_id = f.id " +
                "WHERE l1.user_id = ? " +
                "  AND l2.user_id = ? " +
                "GROUP BY f.id " +
                "ORDER BY COUNT(total_film_likes.id) DESC";
        return jdbcTemplate.query(sql, this::mapRowToFilm, userId, friendId);
    }

    private void saveFilmGenre(Integer filmId, List<Genre> genreList) {
        if (genreList != null && !genreList.isEmpty()) {
            String sqlQueryDeleteGenreForNotExistentIds = "DELETE FROM film_genre WHERE film_id = :filmId " +
                    "AND genre_id NOT IN (:genreIds)";
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource(Map.of(
                    "filmId", filmId,
                    "genreIds", genreList.stream().map(Genre::getId).toArray()));
            namedParameterJdbcTemplate.update(sqlQueryDeleteGenreForNotExistentIds, sqlParameterSource);
            String sqlQueryMergeGenre = "MERGE INTO film_genre(film_id, genre_id) KEY (film_id, genre_id) " +
                    "SELECT ?, ? FROM dual";
            jdbcTemplate.batchUpdate(sqlQueryMergeGenre,
                    genreList,
                    100,
                    (preparedStatement, genre) -> {
                        preparedStatement.setInt(1, filmId);
                        preparedStatement.setInt(2, genre.getId());
                    });
        } else {
            String sqlQueryDeleteAllGenre = "DELETE FROM film_genre WHERE film_id = ?";
            jdbcTemplate.update(sqlQueryDeleteAllGenre, filmId);
        }
    }

    private List<Genre> getFilmGenre(Integer filmId) {
        List<Genre> genres;
        String sqlQuerySelectGenre = "SELECT g.* FROM film_genre f INNER JOIN genre g ON g.id = f.genre_id WHERE film_id = ?";
        genres = jdbcTemplate.query(sqlQuerySelectGenre, new BeanPropertyRowMapper<>(Genre.class), filmId);
        return genres;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        RatingMpa ratingMpa = ratingMpaStorage
                .getById(resultSet.getInt("rating_mpa_id"))
                .orElseThrow();
        List<Genre> genres = this.getFilmGenre(resultSet.getInt("id"));
        List<Director> directors = this.getFilmDirectors(resultSet.getInt("id"));
        return new Film(resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDate("release_date"),
                resultSet.getInt("duration"),
                genres,
                ratingMpa,
                directors);
    }

    private List<Director> getFilmDirectors(Integer filmId) {
        String sqlQuerySelectGenre = "SELECT d.* FROM film_directors f INNER JOIN director d ON d.id = f.director_id WHERE film_id = ?";
        return jdbcTemplate.query(sqlQuerySelectGenre, new BeanPropertyRowMapper<>(Director.class), filmId);
    }

    @Override
    public boolean existsById(int id) {
        String sqlQuery = "SELECT COUNT(1) AS cnt " +
                "FROM film WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class, id) != 0;
    }
}
