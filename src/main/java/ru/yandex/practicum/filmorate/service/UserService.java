package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFilmLike;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserService extends CrudService<User> {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final RecommendServiceImpl<User, Film> recommendService;

    public void addFriend(int userId, int friendId) {
        this.validateIds(userId, friendId);
        this.userStorage.addFriendLink(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        this.validateIds(userId, friendId);
        this.userStorage.deleteFriendLink(userId, friendId);
    }

    public List<User> getFriends(int userId) {
        this.validateIds(userId);
        return this.userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        this.validateIds(userId, otherUserId);
        return this.userStorage.getCommonFriends(userId, otherUserId);
    }

    @Override
    Storage<User> getStorage() {
        return this.userStorage;
    }

    @Override
    String getServiceType() {
        return User.class.getSimpleName();
    }

    public List<Film> getFilmRecommendations(int userId) {
        this.validateIds(userId);
        HashMap<Integer, User> users = new HashMap<>();
        for (User user : userStorage.getAll()) {
            users.put(user.getId(), user);
        }
        HashMap<Integer, Film> films = new HashMap<>();
        for (Film film : filmStorage.getAll()) {
            films.put(film.getId(), film);
        }
        User user = users.get(userId);
        HashMap<User, HashMap<Film, Double>> likeData = new HashMap<>();
        List<UserFilmLike> userFilmLikeList = userStorage.getUserFilmLikes();
        for (UserFilmLike userFilmLike : userFilmLikeList) {
            User currentUser = users.get(userFilmLike.getUserId());
            Film currentFilm = films.get(userFilmLike.getFilmId());
            if (!likeData.containsKey(currentUser)) {
                likeData.put(currentUser, new HashMap<>());
            }
            likeData.get(currentUser).put(currentFilm, 1.0);
        }
        return recommendService.recommend(user, likeData);
    }
}
