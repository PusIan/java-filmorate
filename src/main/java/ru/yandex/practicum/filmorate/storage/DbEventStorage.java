package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventTypeFeed;
import ru.yandex.practicum.filmorate.model.OperationFeed;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbEventStorage implements EventStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Event> getFeed(int userId) {
        String sql = "SELECT * FROM events WHERE user_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToEvent, userId);
    }

    @Override
    public Event create(Event event) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("EVENTS")
                .usingGeneratedKeyColumns("EVENT_ID");
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("timestamp", Timestamp.valueOf(LocalDateTime.now()))
                .addValue("user_id", event.getUserId())
                .addValue("event_type", event.getEventType())
                .addValue("operation", event.getOperation())
                .addValue("entity_id", event.getEntityId());
        event.setEventId(simpleJdbcInsert.executeAndReturnKey(parameters).intValue());
        return event;
    }

    private Event mapRowToEvent(ResultSet rs, int rowNum) throws SQLException {
        Event event = new Event();
        event.setEventId((rs.getInt("event_id")));
        event.setTimestamp(rs.getTimestamp("timestamp").getTime());
        event.setUserId(rs.getInt("user_id"));
        event.setEventType(EventTypeFeed.valueOf(rs.getString("event_type")));
        event.setOperation(OperationFeed.valueOf(rs.getString("operation")));
        event.setEntityId(rs.getInt("entity_id"));
        return event;
    }
}
