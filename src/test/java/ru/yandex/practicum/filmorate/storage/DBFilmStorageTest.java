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
import ru.yandex.practicum.filmorate.model.FilmSearchBy;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
        assertThat(filmStorage.getPopularFilms(1, Optional.empty(), Optional.empty())).isEqualTo(List.of(createdFilm1));

        filmStorage.deleteLike(createdUser.getId(), createdFilm1.getId());
        filmStorage.addLike(createdUser.getId(), createdFilm2.getId());
        assertThat(filmStorage.getPopularFilms(1, Optional.empty(), Optional.empty())).isEqualTo(List.of(createdFilm2));
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
        assertAll(() -> assertThat(filmStorage.filmsDirectorSorted(director1.getId(), "year"))
                        .isEqualTo(manualSort),
                () -> assertThat(filmStorage.filmsDirectorSorted(director1.getId(), "likes"))
                        .isEqualTo(List.of(createdFilm1, createdFilm2)));
    }

    @Test
    @Transactional
    public void testGetFilmByIdsSimpleCaseCorrectResult() {
        Directors director1 = directorStorage.create(Fixtures.getDirector());
        Film film1 = filmStorage.create(Fixtures.getFilm1(List.of(director1)));
        Film film2 = filmStorage.create(Fixtures.getFilm2(List.of(director1)));
        Film film3 = filmStorage.create(Fixtures.getFilm3(List.of(director1)));
        List<Film> expectedFilmList = List.of(film2, film3);
        List<Film> actualFilmList = filmStorage.getFilmsByIds(
                expectedFilmList.stream().map(Film::getId).collect(Collectors.toList()));
        assertThat(actualFilmList).isEqualTo(expectedFilmList);
    }

    @Test
    @Transactional
    public void testGetFilmByIdsSimpleCaseEmptyResult() {
        Directors director1 = directorStorage.create(Fixtures.getDirector());
        Film film1 = filmStorage.create(Fixtures.getFilm1(List.of(director1)));
        Film film2 = filmStorage.create(Fixtures.getFilm2(List.of(director1)));
        List<Film> actualFilmList = filmStorage.getFilmsByIds(List.of(-1));
        assertThat(actualFilmList).isEqualTo(Collections.emptyList());
    }

    @Test
    @Transactional
    public void testFilmSearchByTitle() {
        Directors director1 = directorStorage.create(Fixtures.getDirector());
        filmStorage.create(Fixtures.getFilm1(List.of(director1)));
        Film createdFilm2 = filmStorage.create(Fixtures.getFilm2(List.of(director1)));
        List<Film> expectedFilmList = List.of(createdFilm2);
        List<Film> actualFilmList = filmStorage.searchFilms(createdFilm2.getName(),
                List.of(FilmSearchBy.title));
        assertThat(actualFilmList).isEqualTo(expectedFilmList);
    }

    @Test
    @Transactional
    public void testFilmSearchByDirector() {
        Directors director1 = directorStorage.create(Fixtures.getDirector());
        Directors director2 = directorStorage.create(Fixtures.getDirector2());
        filmStorage.create(Fixtures.getFilm1(List.of(director1)));
        Film createdFilm2 = filmStorage.create(Fixtures.getFilm2(List.of(director2)));
        List<Film> expectedFilmList = List.of(createdFilm2);
        List<Film> actualFilmList = filmStorage.searchFilms(
                createdFilm2.getDirectors().get(0).getName(),
                List.of(FilmSearchBy.director));
        assertThat(actualFilmList).isEqualTo(expectedFilmList);
    }

    @Test
    @Transactional
    public void testFilmSearchByAllCorrectOrder() {
        Directors director1 = directorStorage.create(Fixtures.getDirector());
        Film film1 = filmStorage.create(Fixtures.getFilm1(List.of(director1)));
        Film film2 = filmStorage.create(Fixtures.getFilm2(List.of(director1)));
        Film film3 = filmStorage.create(Fixtures.getFilm3(List.of(director1)));
        User user1 = userStorage.create(Fixtures.getUser1());
        filmStorage.addLike(user1.getId(), film3.getId());
        List<Film> expectedFilmList = List.of(film3, film1, film2);
        List<Film> actualFilmList = filmStorage.searchFilms("film",
                List.of(FilmSearchBy.values()));
        assertThat(actualFilmList).isEqualTo(expectedFilmList);
    }
}
