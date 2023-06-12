package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = ru.yandex.practicum.filmorate.web.starter.FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DBMpaStorageTest {
    private final RatingMpaStorage mpaStorage;

    @Test
    @Transactional
    public void testGetMpaById() {
        RatingMpa ratingMpa = Fixtures.getRatingMpa();
        Optional<RatingMpa> ratingMpaOptional = mpaStorage.getById(ratingMpa.getId());
        assertThat(ratingMpaOptional)
                .isEqualTo(Optional.of(ratingMpa));
    }

    @Test
    @Transactional
    public void testGetAllMpa() {
        assertThat(mpaStorage.getAll()).isEqualTo(Fixtures.getAllRatingMpa());
    }

    @Test
    @Transactional
    public void testGetMpaByIdNotExistentIdEmptyResult() {
        Optional<RatingMpa> mpaOptional = mpaStorage.getById(-1);
        assertThat(mpaOptional).isEmpty();
    }

}