package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DBRatingMpaStorage implements RatingMpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<RatingMpa> getAll() {
        String sqlQuery = "SELECT * FROM rating_mpa";
        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(RatingMpa.class));
    }

    @Override
    public Optional<RatingMpa> getById(int id) {
        if (existsById(id)) {
            String sqlQuery = "select * from rating_mpa WHERE id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, new BeanPropertyRowMapper<>(RatingMpa.class), id));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsById(int id) {
        String sqlQuery = "SELECT COUNT(1) as cnt " +
                "FROM rating_mpa WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class, id) != 0;
    }
}
