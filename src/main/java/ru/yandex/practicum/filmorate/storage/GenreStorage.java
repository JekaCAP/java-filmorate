package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public List<Genre> getGenres() {
        String sql = "SELECT * FROM genres ORDER BY id";
        GenreMapper genreMapper = new GenreMapper();
        return jdbcTemplate.query(sql, genreMapper);
    }

    public Genre getGenreById(Integer genreId) {
        Genre genre;
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres WHERE id = ?", genreId);
        if (genreRows.first()) {
            genre = new Genre(
                    genreRows.getInt("id"),
                    genreRows.getString("name")
            );
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Жанр с ID=" + genreId + " не найден!");
        }
        return genre;
    }

    public void delete(Film film) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", film.getId());
    }

    public void add(Film film) {
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                String sql = "SELECT COUNT(*) FROM genres WHERE id = ?";
                int count = jdbcTemplate.queryForObject(sql, Integer.class, genre.getId());

                if (count == 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Жанр с ID=" + genre.getId() + " не найден!");
                }

                jdbcTemplate.update("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)",
                        film.getId(), genre.getId());
            }
        }
    }

    public List<Genre> getFilmGenres(Long filmId) {
        String sql = "SELECT * FROM film_genres" +
                " INNER JOIN genres ON genre_id = id WHERE film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(
                rs.getInt("genre_id"), rs.getString("name")), filmId
        );
    }

    public void updateGenres(Long filmId, Set<Genre> genres) {
        String deleteGenresQuery = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(deleteGenresQuery, filmId);

        String insertGenresQuery = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";

        for (Genre genre : genres) {
            jdbcTemplate.update(insertGenresQuery, filmId, genre.getId());
        }
    }
}
