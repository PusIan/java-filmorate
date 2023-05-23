package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = ru.yandex.practicum.filmorate.web.starter.FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DBGenreStorageTest {
    private final GenreStorage genreStorage;

    @Test
    public void testGetGenreById() {
        Genre genre = Fixtures.getGenre();
        Optional<Genre> genreOptional = genreStorage.getById(genre.getId());
        assertThat(genreOptional)
                .isEqualTo(Optional.of(genre));
    }

    @Test
    public void testGetAllGenre() {
        assertThat(genreStorage.getAll()).isEqualTo(Fixtures.getAllGenre());
    }

    @Test
    public void testGetGenreByIdNotExistentIdEmptyResult() {
        Optional<Genre> genreOptional = genreStorage.getById(-1);
        assertThat(genreOptional).isEmpty();
    }

}