package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Directors;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DBDirectorStorage implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Directors> getAll() {
        String sqlQuery = "SELECT * FROM director";
        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(Directors.class));
    }

    @Override
    public Optional<Directors> getById(int id) {
        String sqlQuery = "SELECT * FROM director WHERE id = ?";
        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(Directors.class), id)
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
    public Directors create(Directors entity) {
        String sqlQueryDirector = "INSERT INTO director (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmtDirector = connection.prepareStatement(sqlQueryDirector, new String[]{"id"});
            stmtDirector.setString(1, entity.getName());
            return stmtDirector;
        }, keyHolder);
        int directorId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return getById(directorId).orElseThrow();
    }

    @Transactional
    @Override
    public Optional<Directors> update(Directors entity) {
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
