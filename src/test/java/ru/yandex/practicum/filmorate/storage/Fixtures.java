package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Fixtures {
    public static Film getFilm1() {
        return new Film(1,
                "Film 1",
                "Description 1",
                Date.valueOf(LocalDate.now()),
                100,
                new ArrayList<>(List.of(new Genre(1, "Комедия"), new Genre(2, "Драма"))),
                new RatingMpa(1, "G"),
                null);
    }

    public static Film getFilm2() {
        return new Film(2,
                "Film 2",
                "Description 2",
                Date.valueOf(LocalDate.now().minusDays(1)),
                200,
                new ArrayList<>(List.of(new Genre(3, "Мультфильм"))),
                new RatingMpa(2, "PG"),
                null);
    }

    public static User getUser1() {
        return new User(1,
                "1@test.ru",
                "login1",
                "username1",
                Date.valueOf(LocalDate.now().minusYears(20)));
    }

    public static User getUser2() {
        return new User(2,
                "2@test.ru",
                "login2",
                "username2",
                Date.valueOf(LocalDate.now().minusYears(15)));
    }

    public static User getUser3() {
        return new User(3,
                "3@test.ru",
                "login3",
                "username3",
                Date.valueOf(LocalDate.now().minusYears(10)));
    }

    public static Genre getGenre() {
        return getAllGenre().get(0);
    }

    public static List<Genre> getAllGenre() {
        return List.of(new Genre(1, "Комедия"),
                new Genre(2, "Драма"),
                new Genre(3, "Мультфильм"),
                new Genre(4, "Триллер"),
                new Genre(5, "Документальный"),
                new Genre(6, "Боевик"));
    }

    public static List<RatingMpa> getAllRatingMpa() {
        return List.of(new RatingMpa(1, "G"),
                new RatingMpa(2, "PG"),
                new RatingMpa(3, "PG-13"),
                new RatingMpa(4, "R"),
                new RatingMpa(5, "NC-17"));
    }

    public static RatingMpa getRatingMpa() {
        return getAllRatingMpa().get(0);
    }

    public static List<Directors> getDirectorList() {
        return List.of(new Directors(1, "Luck"),
                new Directors(2, "Bortko"),
                new Directors(3, "Reuel Tolkien"));
    }

    public static Directors getDirector() {
        return new Directors(2, "Bortko");
    }

    public static Directors getDirector2() {
        return new Directors(5, "Trail");
    }
}
