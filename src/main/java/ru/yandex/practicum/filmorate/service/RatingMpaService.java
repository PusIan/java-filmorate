package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;
import ru.yandex.practicum.filmorate.storage.ReadStorage;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingMpaService extends ReadService<RatingMpa> {
    private final RatingMpaStorage ratingMpaStorage;

    @Override
    protected ReadStorage<RatingMpa> getReadStorage() {
        return this.ratingMpaStorage;
    }

    @Override
    String getServiceType() {
        return RatingMpa.class.getSimpleName();
    }
}
