package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.AfterDate;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class Film {

    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @Size(max = 200, message = "Максимальная длина 200 символов")
    private String description;

    @NotNull
    @AfterDate(value = "1895-12-28")
    private LocalDate releaseDate;

    @NotNull
    @Positive
    private int duration;

    private Set<Long> likes;

    private Mpa mpa;

    private Set<Genre> genres = new HashSet<>();

    @JsonSetter
    public void setGenres(Set<Genre> genres) {
        this.genres = genres.stream()
                .sorted(Comparator.comparingInt(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}


