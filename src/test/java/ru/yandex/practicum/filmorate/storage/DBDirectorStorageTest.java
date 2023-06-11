package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Directors;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = ru.yandex.practicum.filmorate.web.starter.FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DBDirectorStorageTest {

    private final DirectorStorage directorStorage;

    @AfterEach
    private void clear() {
        for (Directors director : directorStorage.getAll()) {
            System.out.println(director);
            System.out.println(director.getId());
            directorStorage.delete(director.getId());
        }
    }

    @Test
    @Transactional
    public void testCreateGetDirector() {
        Directors directors = Fixtures.getDirector();
        Directors createDirector = directorStorage.create(Fixtures.getDirector());
        directors.setId(createDirector.getId());
        assertThat(directorStorage.getById(createDirector.getId())).hasValue(directors);
    }

    @Test
    @Transactional
    public void testUpdateGetDirector() {
        Directors createDirector = directorStorage.create(Fixtures.getDirector());
        Directors directors = Fixtures.getDirector();
        directors.setId(createDirector.getId());
        directorStorage.update(directors);
        assertThat(directorStorage.getById(createDirector.getId())).hasValue(directors);
    }

    @Test
    @Transactional
    public void testDeleteFilm() {
        Directors createDirector = directorStorage.create(Fixtures.getDirector());
        directorStorage.delete(createDirector.getId());
        assertThat(directorStorage.getById(createDirector.getId())).isEmpty();
    }

    @Test
    @Transactional
    public void testGetDirectorsAll() {
        Directors createDirector = directorStorage.create(Fixtures.getDirector());
        Directors createDirector2 = directorStorage.create(Fixtures.getDirector2());
        assertThat(directorStorage.getAll()).isEqualTo(List.of(createDirector, createDirector2));
    }
}
