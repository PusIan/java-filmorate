package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFilmLike;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DBUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User entity) {
        String sqlQueryFilm = "INSERT INTO user_(email, login, name, birthday) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmtFilm = connection.prepareStatement(sqlQueryFilm, new String[]{"id"});
            stmtFilm.setString(1, entity.getEmail());
            stmtFilm.setString(2, entity.getLogin());
            stmtFilm.setString(3, entity.getName());
            stmtFilm.setDate(4, entity.getBirthday());
            return stmtFilm;
        }, keyHolder);
        int userId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return this.getById(userId).orElseThrow();
    }

    @Override
    public Optional<User> update(User entity) {
        String sqlQuery = "UPDATE user_ SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sqlQuery, entity.getEmail(), entity.getLogin(), entity.getName(), entity.getBirthday(), entity.getId());
        return this.getById(entity.getId());
    }

    @Override
    public void delete(int id) {
        String sqlQuery = "DELETE FROM user_ WHERE id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT * FROM user_";
        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public Optional<User> getById(int id) {
        String sqlQuery = "SELECT * FROM user_ WHERE id = ?";
        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(User.class), id)
                .stream()
                .findFirst();
    }

    @Override
    public boolean existsById(int id) {
        String sqlQuery = "SELECT COUNT(1) AS cnt FROM user_ WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class, id) != 0;
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherUserId) {
        String sqlQuery = "SELECT * FROM user_\n" +
                "WHERE ID IN (\n" +
                "SELECT FR1.USER_FRIEND_ID from user_ U1\n" +
                "    INNER JOIN FRIEND_REQUEST FR1 on U1.ID = FR1.USER_INITIATOR_ID\n" +
                "    WHERE U1.ID = ?\n" +
                "INTERSECT\n" +
                "SELECT FR2.USER_FRIEND_ID from user_ U2\n" +
                "    INNER JOIN FRIEND_REQUEST FR2 on U2.ID = FR2.USER_INITIATOR_ID\n" +
                "WHERE U2.ID = ?)\n";
        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(User.class), userId, otherUserId);
    }

    @Override
    public void addFriendLink(int userId, int friendId) {
        String sqlQuery = "INSERT INTO friend_request (user_initiator_id, user_friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void deleteFriendLink(int userId, int friendId) {
        String sqlQuery = "delete from friend_request where user_initiator_id = ? AND user_friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> getFriends(int userId) {
        String sqlQuery = "SELECT f.* FROM friend_request fr" +
                " INNER JOIN user_ f ON f.id = fr.user_friend_id" +
                " WHERE user_initiator_id = ?";
        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(User.class), userId);
    }

    @Override
    public List<UserFilmLike> getUserFilmLikes() {
        String sqlQuery = "SELECT u.id userid, l.film_id filmid FROM user_ u\n" +
                "    INNER JOIN like_ l ON u.ID = l.USER_ID";
        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(UserFilmLike.class));
    }
}
