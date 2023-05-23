package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = ru.yandex.practicum.filmorate.web.starter.FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DBUserStorageTest {
    private final UserStorage userStorage;

    @Test
    @Transactional
    public void testCreateGetUser() {
        User user = Fixtures.getUser1();
        User createdUser = userStorage.create(Fixtures.getUser1());
        user.setId(createdUser.getId());
        assertThat(userStorage.getById(createdUser.getId())).hasValue(user);
    }

    @Test
    @Transactional
    public void testUpdateGetUser() {
        User createdUser = userStorage.create(Fixtures.getUser1());
        User updatedUser = Fixtures.getUser2();
        updatedUser.setId(createdUser.getId());
        userStorage.update(updatedUser);
        assertThat(userStorage.getById(createdUser.getId())).hasValue(updatedUser);
    }

    @Test
    @Transactional
    public void testDeleteUser() {
        User createdUser = userStorage.create(Fixtures.getUser1());
        userStorage.delete(createdUser.getId());
        assertThat(userStorage.getById(createdUser.getId())).isEmpty();
    }

    @Test
    @Transactional
    public void testGetUserAll() {
        User createdUser1 = userStorage.create(Fixtures.getUser1());
        User createdUser2 = userStorage.create(Fixtures.getUser2());
        assertThat(userStorage.getAll()).isEqualTo(List.of(createdUser1, createdUser2));
    }

    @Test
    @Transactional
    public void testFriendLink() {
        User createdUser1 = userStorage.create(Fixtures.getUser1());
        User createdUser2 = userStorage.create(Fixtures.getUser2());
        userStorage.addFriendLink(createdUser1.getId(), createdUser2.getId());
        assertAll(
                () -> assertThat(userStorage.getFriends(createdUser1.getId())).isEqualTo(List.of(createdUser2)),
                () -> assertThat(userStorage.getFriends(createdUser2.getId())).isEqualTo(Collections.emptyList())
        );

        userStorage.deleteFriendLink(createdUser1.getId(), createdUser2.getId());
        assertAll(
                () -> assertThat(userStorage.getFriends(createdUser1.getId())).isEqualTo(Collections.emptyList()),
                () -> assertThat(userStorage.getFriends(createdUser2.getId())).isEqualTo(Collections.emptyList())
        );
    }

    @Test
    @Transactional
    public void testCommonFriends() {
        User createdUser1 = userStorage.create(Fixtures.getUser1());
        User createdUser2 = userStorage.create(Fixtures.getUser2());
        User createdUserCommonFriend = userStorage.create(Fixtures.getUser3());
        assertThat(userStorage.getCommonFriends(createdUser1.getId(), createdUser2.getId()))
                .isEqualTo(Collections.emptyList());

        userStorage.addFriendLink(createdUser1.getId(), createdUserCommonFriend.getId());
        userStorage.addFriendLink(createdUser2.getId(), createdUserCommonFriend.getId());
        assertThat(userStorage.getCommonFriends(createdUser1.getId(), createdUser2.getId()))
                .isEqualTo(List.of(createdUserCommonFriend));

    }
}
