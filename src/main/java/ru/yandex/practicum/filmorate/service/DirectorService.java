package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DirectorService extends CrudService<Director> {

    private final DirectorStorage directorStorage;

    public void deleteDirector(int id) {
        this.directorStorage.delete(id);
    }

    @Override
    Storage<Director> getStorage() {
        return  this.directorStorage;
    }

    @Override
    String getServiceType() {
        return Director.class.getSimpleName();
    }
}
