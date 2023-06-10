package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Directors;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DirectorService extends CrudService<Directors> {

    private final DirectorStorage directorStorage;
    @Override
    Storage<Directors> getStorage() {
        return this.directorStorage;
    }

    @Override
    String getServiceType() {
        return Directors.class.getSimpleName();
    }
}
