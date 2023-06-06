package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DBDirectorStorage implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Director> getAll() {
        String sqlQuery = "SELECT * FROM director";
        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(Director.class));
    }

    @Override
    public Optional<Director> getById(int id) {
        String sqlQuery = "select * from director WHERE id = ?";
        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(Director.class), id)
                .stream()
                .findFirst();
    }

    @Override
    public boolean existsById(int id) {
        String sqlQuery = "SELECT COUNT(1) as cnt " +
                "FROM director WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class, id) != 0;
    }

    @Transactional
    @Override
    public Director create(Director entity) {
        String sqlQueryDirector = "INSERT INTO director (name) " + "VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmtFilm = connection.prepareStatement(sqlQueryDirector, new String[]{"id"});
            stmtFilm.setString(1, entity.getName());
            return stmtFilm;
        }, keyHolder);
        int filmId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return this.getById(filmId).orElseThrow();
    }

    @Transactional
    @Override
    public Optional<Director> update(Director entity) {
        String sqlQuery = "UPDATE director SET name = ? WHERE id = ?";
        jdbcTemplate.update(sqlQuery, entity.getName(), entity.getId());
        return this.getById(entity.getId());
    }

    @Override
    public void delete(int id) {
        String sqlQuery = "DELETE FROM director WHERE id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }
}
