package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService extends CrudService<User> {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userId, int friendId) {
        this.userStorage.validateId(userId)
                .orElseThrow(() -> getNoDataFoundException(userId));
        this.userStorage.validateId(friendId)
                .orElseThrow(() -> getNoDataFoundException(friendId));
        this.userStorage.getById(userId)
                .get()
                .getFriends()
                .add(friendId);
        this.userStorage.getById(friendId)
                .get()
                .getFriends()
                .add(userId);
    }

    public void deleteFriend(int userId, int friendId) {
        this.userStorage.validateId(userId)
                .orElseThrow(() -> getNoDataFoundException(userId));
        this.userStorage.validateId(friendId)
                .orElseThrow(() -> getNoDataFoundException(friendId));
        this.userStorage.getById(userId)
                .get()
                .getFriends()
                .remove(friendId);
        this.userStorage.getById(friendId)
                .get()
                .getFriends()
                .remove(userId);
    }

    public List<User> getFriends(int userId) {
        return this.userStorage
                .getById(userId)
                .orElseThrow(() -> getNoDataFoundException(userId))
                .getFriends()
                .stream()
                .map(userStorage::getById)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        Set<Integer> friendsForUser = new HashSet<>(userStorage.getById(userId)
                .orElseThrow(() -> getNoDataFoundException(userId)).getFriends());
        Set<Integer> friendsForOtherUser = new HashSet<>(userStorage.getById(otherUserId)
                .orElseThrow(() -> getNoDataFoundException(otherUserId)).getFriends());
        return friendsForUser
                .stream()
                .filter(friendsForOtherUser::contains)
                .map(userStorage::getById)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    Storage<User> getStorage() {
        return this.userStorage;
    }

    @Override
    String getServiceType() {
        return User.class.getSimpleName();
    }
}
