package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.exception.ValidationException.FILM_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.exception.ValidationException.FILM_NOT_FOUND;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    Map<Long, Film> films = new HashMap<>();
    private Long uniqId = 0L;

    @GetMapping
    public List<Film> getFimls() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilms(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            throw new ValidationException(
                    String.format(FILM_ALREADY_EXISTS, film));
        }
        film.setId(++uniqId);
        films.put(film.getId(), film);
        log.info("Успешно добавлен фильм: {}.", film);

        return film;
    }

    @PutMapping
    public Film updateFilms(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException(
                    String.format(FILM_NOT_FOUND, film));
        }
        films.put(film.getId(), film);
        log.info("Фильм успешно обновлён: {}.", film);

        return film;
    }


}
