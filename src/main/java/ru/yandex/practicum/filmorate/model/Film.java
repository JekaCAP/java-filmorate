package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.AfterDate;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
public class Film {

    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @Size(max = 200)
    private String description;
    @NotNull
    @AfterDate(value = "1895-12-28")
    private LocalDate releaseDate;
    @NotNull
    @Positive
    private int duration;

}


