package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.exception.ValidationException.FILM_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.exception.ValidationException.FILM_NOT_FOUND;

@Slf4j
@Service
public class FilmService {

    private final Map<Long, Film> films = new HashMap<>();
    private Long uniqId = 0L;

    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    public Film createFilm(Film film) {
        if (films.containsKey(film.getId())) {
            throw new ValidationException(String.format(FILM_ALREADY_EXISTS, film));
        }
        film.setId(++uniqId);
        films.put(film.getId(), film);
        log.info("Фильм добавлен: {}", film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException(String.format(FILM_NOT_FOUND, film));
        }
        films.put(film.getId(), film);
        log.info("Фильм обновлен: {}", film);
        return film;
    }
}
