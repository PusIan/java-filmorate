package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = ru.yandex.practicum.filmorate.web.starter.FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DBDirectorStorageTest {

    private final DirectorStorage directorStorage;

    @Test
    @Transactional
    public void testCreateGetDirector() {
        Director director = Fixtures.getDirector();
        Director createDirector = directorStorage.create(Fixtures.getDirector());
        director.setId(createDirector.getId());
        assertThat(directorStorage.getById(createDirector.getId())).hasValue(director);
    }

    @Test
    @Transactional
    public void testUpdateGetDirector() {
        Director createDirector = directorStorage.create(Fixtures.getDirector());
        Director director = Fixtures.getDirector();
        director.setId(createDirector.getId());
        directorStorage.update(director);
        assertThat(directorStorage.getById(createDirector.getId())).hasValue(director);
    }

    @Test
    @Transactional
    public void testDeleteFilm() {
        Director createDirector = directorStorage.create(Fixtures.getDirector());
        directorStorage.delete(createDirector.getId());
        assertThat(directorStorage.getById(createDirector.getId())).isEmpty();
    }

    @Test
    @Transactional
    public void testGetDirectorsAll() {
        Director createDirector = directorStorage.create(Fixtures.getDirector());
        Director createDirector2 = directorStorage.create(Fixtures.getDirector2());
        assertThat(directorStorage.getAll()).isEqualTo(List.of(createDirector, createDirector2));
    }
}
