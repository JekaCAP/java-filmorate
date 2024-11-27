package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmControllerTest {
    private Film film;
    private static Validator validator;
    private Set<ConstraintViolation<Film>> violations;

    @BeforeEach
    public void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        film = new Film(1L, "Название", "описание, " , 120, LocalDate.of(2004, 1, 18));
        violations = validator.validate(film);
    }

    @Test
    public void testAddFilm() {
        violations = validator.validate(film);
        assertEquals(0, violations.size(), "Ошибки валидации");
        List<Film> films = new ArrayList<>();
        films.add(film);
        assertTrue(films.contains(film), "Фильм не был добавлен в список");
    }

    // проверка  при пустом названии у фильма
    @Test
    public void testEmptyFilmName() {
        film.setName("");
        violations = validator.validate(film);
        assertEquals(1, violations.size(), "Ошибки валидации");
    }

    // продолжительность фильма равна нулю
    @Test
    public void testInvalidDuration() {
        film.setDuration(-1);
        violations = validator.validate(film);
        assertEquals(1, violations.size(), "Ошибки валидации");
    }

    @Test
    public void testIncorrectRelease() {
        film.setReleaseDate(LocalDate.parse(String.valueOf(LocalDate.of(1895, 12, 27))));
        violations = validator.validate(film);
        assertEquals(1, violations.size(), "Ошибки валидации");
    }

    @Test
    public void testEmptyDescription() {
        film.setDescription("");
        violations = validator.validate(film);
        assertEquals(0, violations.size(), "Ошибки валидации");
    }

    // неправильная продолжительность фильма
    @Test
    public void testIncorrectDurationIsNegative() {
        film.setDuration(-1);
        violations = validator.validate(film);
        assertEquals(1, violations.size(), "Ошибки валидации");
    }
}
