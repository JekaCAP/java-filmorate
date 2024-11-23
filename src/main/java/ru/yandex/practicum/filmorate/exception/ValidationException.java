package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends RuntimeException {
    public static final String FILM_ALREADY_EXISTS = "Фильм уже добавлен ранее >%s";
    public static final String FILM_NOT_FOUND = "Фильм не найден >%s";
    public static final String USER_ALREADY_EXISTS = "Пользователь уже добавлен ранее >%s";
    public static final String USER_NOT_FOUND = "Пользователь не найден >%s";

    public ValidationException(String message) {
        super(message);
    }
}
