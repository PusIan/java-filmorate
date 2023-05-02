package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage extends InMemoryStorage<User> implements UserStorage {
    @Override
    public List<User> getCommonFriends(int userId, int otherUserId) {
        if (this.existsById(userId) && this.existsById(otherUserId)) {
            Set<Integer> friendsForUser = new HashSet<>(this.getById(userId).get().getFriends());
            Set<Integer> friendsForOtherUser = new HashSet<>(this.getById(otherUserId).get().getFriends());
            return friendsForUser
                    .stream()
                    .filter(friendsForOtherUser::contains)
                    .map(this::getById)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public void addFriendLink(int userId, int friendId) {
        if (this.existsById(userId) && this.existsById(friendId)) {
            this.getById(userId).get().getFriends().add(friendId);
        }
    }

    @Override
    public void deleteFriendLink(int userId, int friendId) {
        if (this.existsById(userId) && this.existsById(friendId)) {
            this.getById(userId).get().getFriends().remove(friendId);
        }
    }

    @Override
    public List<User> getFriends(int userId) {
        if (this.existsById(userId)) {
            return this
                    .getById(userId)
                    .get()
                    .getFriends()
                    .stream()
                    .map(this::getById)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
