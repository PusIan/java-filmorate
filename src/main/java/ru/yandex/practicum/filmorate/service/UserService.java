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
    private final RecommendServiceImpl<Integer, Integer> recommendService;

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
        // HashMap<userId, HashMap<filmId, Integer>>
        HashMap<Integer, HashMap<Integer, Integer>> likeData = new HashMap<>();
        List<UserFilmLike> userFilmLikeList = userStorage.getUserFilmLikes();
        for (UserFilmLike userFilmLike : userFilmLikeList) {
            if (!likeData.containsKey(userFilmLike.getUserId())) {
                likeData.put(userFilmLike.getUserId(), new HashMap<>());
            }
            likeData.get(userFilmLike.getUserId()).put(userFilmLike.getFilmId(), 1);
        }
        List<Integer> filmIds = recommendService.recommend(userId, likeData);
        return filmStorage.getFilmsByIds(filmIds);
    }
}
