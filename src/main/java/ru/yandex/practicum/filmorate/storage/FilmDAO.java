package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmColumn;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Primary
@Component
@RequiredArgsConstructor
public class FilmDAO implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final LikeStorage likeStorage;

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * FROM films";
        List<FilmColumn> filmColumns = jdbcTemplate.query(sql, new FilmMapper());

        return filmColumns.stream()
                .map(this::fromColumnsToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Film createFilm(Film film) {
        // Проверка существования rating_id
        Integer mpaId = film.getMpa().getId();
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM ratings_mpa WHERE id = ?",
                Integer.class,
                mpaId
        );

        if (count == null || count == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Рейтинг с ID=" + mpaId + " не найден!");
        }

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", film.getName());
        parameters.put("description", film.getDescription());
        parameters.put("duration", film.getDuration());
        parameters.put("release_date", film.getReleaseDate());
        parameters.put("rating_id", mpaId);

        long generatedId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        film.setId(generatedId);
        film.setMpa(mpaStorage.getMpaById(mpaId));
        genreStorage.add(film);

        return film;
    }



    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, " +
                "rating_id = ? WHERE id = ?";
        int updateCount = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (updateCount != 0) {
            film.setMpa(mpaStorage.getMpaById(film.getMpa().getId()));
            genreStorage.updateGenres(film.getId(), film.getGenres());
            return film;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
    }

    @Override
    public Film getFilmById(Long filmId) {
        String sqlQuery = "SELECT * FROM films WHERE id = ?";
        try {
            FilmColumn filmColumn = jdbcTemplate.queryForObject(sqlQuery, new FilmMapper(), filmId);
            return fromColumnsToDto(Objects.requireNonNull(filmColumn));
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
    }

    @Override
    public Film deleteFilm(Long filmId) {
        Film film = getFilmById(filmId);
        String sqlQuery = "DELETE FROM films WHERE id = ? ";
        if (jdbcTemplate.update(sqlQuery, filmId) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с ID=" + filmId + " не найден!");
        }
        return film;
    }


    private Film fromColumnsToDto(FilmColumn filmColumn) {
        Film film = new Film();
        film.setId(filmColumn.getId());
        film.setName(filmColumn.getName());
        film.setDescription(filmColumn.getDescription());
        film.setDuration(filmColumn.getDuration());
        film.setReleaseDate(filmColumn.getReleaseDate());
        film.setMpa(mpaStorage.getMpaById(filmColumn.getMpaId()));
        film.setGenres(new HashSet<>(genreStorage.getFilmGenres(film.getId())));
        film.setLikes(new HashSet<>(likeStorage.getLikes(filmColumn.getId())));
        return film;
    }
}
