package ru.yandex.practicum.filmorate.storage.interfaces;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();

    User createUser(@Valid User user);

    User updateUser(@Valid User user);

    User getUserById(Long id);

    User deleteUser(Long id);
}
