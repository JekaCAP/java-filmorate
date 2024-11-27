package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage  {
    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    User getUserById(Long id);

    User deleteUser(Long id);
}