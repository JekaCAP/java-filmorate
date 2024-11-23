package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {

    private Long id;
    @NotNull
    @NotBlank
    @Email(message = "Электронная почта должна быть корректной.")
    private String email;
    @NotBlank(message = "Логин не может быть пустым.")
    @Pattern(regexp = "^\\S+$", message = "Логин не может содержать пробелы.")
    private String login;
    private String name;
    @NotNull
    @Past(message = "Дата рождения должна быть в прошлом.")
    private LocalDate birthday;

}
