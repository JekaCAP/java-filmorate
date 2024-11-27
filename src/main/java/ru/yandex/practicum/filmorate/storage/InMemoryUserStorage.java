package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private Long uniqueId = 0L;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        if (users.containsKey(user.getId())) {
            throw new ResponseStatusException(HttpStatus
                    .CONFLICT, "Пользователь c id=" + user.getId() + " уже добавлен ранее");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(++uniqueId);
        users.put(user.getId(), user);
        log.info("Пользователь добавлен: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ResponseStatusException(HttpStatus
                    .NOT_FOUND, "Пользователь с id=" + user.getId() + " не найден!");
        }
        users.put(user.getId(), user);
        log.info("Пользователь обновлен: {}", user);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с id=" + id + " не найден!");
        }
        return users.get(id);
    }

    @Override
    public User deleteUser(Long id) {
        if (!users.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с id=" + id + " не найден!");
        }
        for(User user : users.values()) {
            user.getFriends().remove(id);
        }
        return users.remove(id);
    }
}