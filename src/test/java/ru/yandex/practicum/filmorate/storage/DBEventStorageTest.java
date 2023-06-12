package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ru.yandex.practicum.filmorate.web.starter.FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DBEventStorageTest {
    private final EventStorage eventStorage;
    private final UserStorage userStorage;

    @Test
    @Transactional
    public void testCreateEventAndGetFeed() {
        userStorage.create(Fixtures.getUser1());
        Event event = Fixtures.getEvent();
        eventStorage.create(event);
        List<Event> events = eventStorage.getFeed(event.getUserId());

        assertEquals(events.size(), 1);
        assertTrue(events.contains(event));
    }

}
