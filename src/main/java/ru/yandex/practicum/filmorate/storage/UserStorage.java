package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage extends Storage<User> {
    List<User> getCommonFriends(int userId, int otherUserId);

    void addFriendLink(int userId, int friendId);

    void deleteFriendLink(int userId, int friendId);

    List<User> getFriends(int userId);
}
