package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.exception.ValidationException.USER_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.exception.ValidationException.USER_NOT_FOUND;

@Slf4j
@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();
    private Long uniqueId = 0L;

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User createUser(User user) {
        if (users.containsKey(user.getId())) {
            throw new ValidationException(String.format(USER_ALREADY_EXISTS, user));
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(++uniqueId);
        users.put(user.getId(), user);
        log.info("Пользователь добавлен: {}", user);
        return user;
    }

    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException(String.format(USER_NOT_FOUND, user));
        }
        users.put(user.getId(), user);
        log.info("Пользователь обновлен: {}", user);
        return user;
    }
}
