package ru.yandex.practicum.filmorate.storage.interfaces;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAllFilms();

    Film createFilm(@Valid Film film);

    Film updateFilm(@Valid Film film);

    Film getFilmById(Long id);

    Film deleteFilm(Long id);
}
