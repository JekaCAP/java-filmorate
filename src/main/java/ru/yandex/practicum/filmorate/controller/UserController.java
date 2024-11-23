package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.exception.ValidationException.USER_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.exception.ValidationException.USER_NOT_FOUND;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    Map<Long, User> users = new HashMap<>();
    private Long uniqueId = 0L;

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            throw new ValidationException(String.format(USER_ALREADY_EXISTS, user));
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(++uniqueId);
        users.put(user.getId(), user);
        log.info("Успешно добавлен пользователь: {}.", user);

        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException(String.format(USER_NOT_FOUND, user));
        }
        users.put(user.getId(), user);
        log.info("Пользователь успешно обновлён: {}.", user);

        return user;
    }
}
