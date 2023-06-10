package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collections;
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

    @BeforeAll
    private void create() {
        directorStorage.create(Fixtures.getDirector());
        directorStorage.create(Fixtures.getDirector2());
    }

    @AfterAll
    private void clear() {
        directorStorage.delete(Fixtures.getDirector().getId());
        directorStorage.delete(Fixtures.getDirector2().getId());
    }

    @Test
    @Transactional
    public void testCreateGetFilm() {
        Film film = Fixtures.getFilm1();
        Film createdFilm = filmStorage.create(Fixtures.getFilm1());
        film.setId(createdFilm.getId());
        assertThat(filmStorage.getById(createdFilm.getId())).hasValue(film);
    }

    @Test
    @Transactional
    public void testUpdateGetFilm() {
        Film createdFilm = filmStorage.create(Fixtures.getFilm1());
        Film updatedFilm = Fixtures.getFilm2();
        updatedFilm.setId(createdFilm.getId());
        filmStorage.update(updatedFilm);
        assertThat(filmStorage.getById(createdFilm.getId())).hasValue(updatedFilm);
    }

    @Test
    @Transactional
    public void testDeleteFilm() {
        Film createdFilm = filmStorage.create(Fixtures.getFilm1());
        filmStorage.delete(createdFilm.getId());
        assertThat(filmStorage.getById(createdFilm.getId())).isEmpty();
    }

    @Test
    @Transactional
    public void testGetFilmAll() {
        Film createdFilm1 = filmStorage.create(Fixtures.getFilm1());
        Film createdFilm2 = filmStorage.create(Fixtures.getFilm2());
        assertThat(filmStorage.getAll()).isEqualTo(List.of(createdFilm1, createdFilm2));
    }

    @Test
    @Transactional
    public void testLikes() {
        Film createdFilm1 = filmStorage.create(Fixtures.getFilm1());
        Film createdFilm2 = filmStorage.create(Fixtures.getFilm2());
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
        Film createdFilm1 = filmStorage.create(Fixtures.getFilm3());
        Film createdFilm2 = filmStorage.create(Fixtures.getFilm2());
        filmStorage.addLike(userStorage.create(Fixtures.getUser1()).getId(), createdFilm1.getId());
        List<Film> manualSort = Stream.of(createdFilm1, createdFilm2)
                .sorted(Comparator.comparing(Film::getReleaseDate).reversed()).collect(Collectors.toList());
        assertThat(filmStorage.filmsDirectorSorted(2, "year")).isEqualTo(manualSort);
        assertThat(filmStorage.filmsDirectorSorted(2, "likes")).isEqualTo(List.of(createdFilm1, createdFilm2));
    }

    @Test
    @Transactional
    public void testGetFilmByIdsSimpleCaseCorrectResult() {
        Film film1 = filmStorage.create(Fixtures.getFilm1());
        Film film2 = filmStorage.create(Fixtures.getFilm2());
        Film film3 = filmStorage.create(Fixtures.getFilm3());
        List<Film> expectedFilmList = List.of(film2, film3);
        List<Film> actualFilmList = filmStorage.getFilmsByIds(
                expectedFilmList.stream().map(Film::getId).collect(Collectors.toList()));
        assertThat(actualFilmList).isEqualTo(expectedFilmList);
    }

    @Test
    @Transactional
    public void testGetFilmByIdsSimpleCaseEmptyResult() {
        Film film1 = filmStorage.create(Fixtures.getFilm1());
        Film film2 = filmStorage.create(Fixtures.getFilm2());
        List<Film> actualFilmList = filmStorage.getFilmsByIds(List.of(-1));
        assertThat(actualFilmList).isEqualTo(Collections.emptyList());
    }
}
