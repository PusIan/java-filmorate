package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Directors;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = ru.yandex.practicum.filmorate.web.starter.FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DBFilmStorageTest {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final DirectorStorage directorStorage;

    @Test
    @Transactional
    public void testCreateGetFilm() {
        Directors director1 = directorStorage.create(Fixtures.getDirector());
        Directors director2 = directorStorage.create(Fixtures.getDirector2());
        Film film = Fixtures.getFilm1(List.of(director1));
        Film createdFilm = filmStorage.create(Fixtures.getFilm1(List.of(director1, director2)));
        film.setId(createdFilm.getId());
        assertThat(filmStorage.getById(createdFilm.getId())).hasValue(film);
    }

    @Test
    @Transactional
    public void testUpdateGetFilm() {
        Directors director1 = directorStorage.create(Fixtures.getDirector());
        Directors director2 = directorStorage.create(Fixtures.getDirector2());
        Film createdFilm = filmStorage.create(Fixtures.getFilm1(List.of(director1)));
        Film updatedFilm = Fixtures.getFilm2(List.of(director2));
        updatedFilm.setId(createdFilm.getId());
        filmStorage.update(updatedFilm);
        assertThat(filmStorage.getById(createdFilm.getId())).hasValue(updatedFilm);
    }

    @Test
    @Transactional
    public void testDeleteFilm() {
        Directors director1 = directorStorage.create(Fixtures.getDirector());
        Film createdFilm = filmStorage.create(Fixtures.getFilm1(List.of(director1)));
        filmStorage.delete(createdFilm.getId());
        assertThat(filmStorage.getById(createdFilm.getId())).isEmpty();
    }

    @Test
    @Transactional
    public void testGetFilmAll() {
        Directors director1 = directorStorage.create(Fixtures.getDirector());
        Directors director2 = directorStorage.create(Fixtures.getDirector2());
        Film createdFilm1 = filmStorage.create(Fixtures.getFilm1(List.of(director1)));
        Film createdFilm2 = filmStorage.create(Fixtures.getFilm2(List.of(director2)));
        assertThat(filmStorage.getAll()).isEqualTo(List.of(createdFilm1, createdFilm2));
    }

    @Test
    @Transactional
    public void testLikes() {
        Directors director1 = directorStorage.create(Fixtures.getDirector());
        Directors director2 = directorStorage.create(Fixtures.getDirector2());
        Film createdFilm1 = filmStorage.create(Fixtures.getFilm1(List.of(director1)));
        Film createdFilm2 = filmStorage.create(Fixtures.getFilm2(List.of(director2)));
        User createdUser = userStorage.create(Fixtures.getUser1());
        filmStorage.addLike(createdUser.getId(), createdFilm1.getId());
        assertThat(filmStorage.getPopularFilms(1)).isEqualTo(List.of(createdFilm1));

        filmStorage.deleteLike(createdUser.getId(), createdFilm1.getId());
        filmStorage.addLike(createdUser.getId(), createdFilm2.getId());
        assertThat(filmStorage.getPopularFilms(1)).isEqualTo(List.of(createdFilm2));
    }

    @Test
    @Transactional
    public void testSortFromDirector() {
        Directors director1 = directorStorage.create(Fixtures.getDirector());
        Film createdFilm1 = filmStorage.create(Fixtures.getFilm3(List.of(director1)));
        Film createdFilm2 = filmStorage.create(Fixtures.getFilm2(List.of(director1)));
        User createdUser = userStorage.create(Fixtures.getUser1());
        filmStorage.addLike(createdUser.getId(), createdFilm1.getId());
        List<Film> manualSort = Stream.of(createdFilm1, createdFilm2)
                .sorted(Comparator.comparing(Film::getReleaseDate).reversed()).collect(Collectors.toList());
        assertThat(filmStorage.filmsDirectorSorted(director1.getId(), "year"))
                .isEqualTo(manualSort);
        assertThat(filmStorage.filmsDirectorSorted(director1.getId(), "likes"))
                .isEqualTo(List.of(createdFilm1, createdFilm2));
    }
}
