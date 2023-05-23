package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.ReadStorage;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreService extends ReadService<Genre> {
    private final GenreStorage genreStorage;

    @Override
    ReadStorage<Genre> getReadStorage() {
        return this.genreStorage;
    }

    @Override
    String getServiceType() {
        return Genre.class.getSimpleName();
    }
}
