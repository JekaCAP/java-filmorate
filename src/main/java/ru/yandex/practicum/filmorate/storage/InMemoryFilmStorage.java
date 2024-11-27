package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private Long uniqId = 0L;

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        if (films.containsKey(film.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Фильм c id=" + film.getId() + " добавлен ранее");
        }
        film.setId(++uniqId);
        films.put(film.getId(), film);
        log.info("Фильм добавлен: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с id=" + film.getId() + " не найден!");
        }
        films.put(film.getId(), film);
        log.info("Фильм обновлен: {}", film);
        return film;
    }

    @Override
    public Film getFilmById(Long id) {
        if (!films.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с id=" + id + " не найден!");
        }
        return films.get(id);
    }

    @Override
    public Film deleteFilm(Long id) {
        if (!films.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с id=" + id + " не найден!");
        }
        return films.remove(id);
    }
}
