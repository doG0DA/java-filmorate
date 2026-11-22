package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAll() {
        log.debug("Запрос на вывод всех фильмов!");
        return films.values();
    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody Film newFilm) {
        log.debug("Запрос на добавление фильма: {} ", newFilm.getName());
        isValidFilm(newFilm);

        newFilm.setId(getNextId());
        films.put(newFilm.getId(), newFilm);

        log.info("Фильм с id={} успешно добавлен", newFilm.getId());
        return newFilm;
    }

    @PutMapping
    public Film changeFilm(@Valid @RequestBody Film newFilm) {
        log.debug("Запрос на обновление данных фильма: {}", newFilm);
        if (newFilm.getId() == null || newFilm.getId() <= 0) {
            log.warn("Не указан ID обновленного фильма");
            throw new ConditionsNotMetException("ID должен быть указан!");
        }

        isValidFilm(newFilm);

        Film oldSettigs = films.get(newFilm.getId());
        if (oldSettigs == null) {
            log.warn("Невозможно произвести обновление, фильм c ID={} не был найден", newFilm.getId());
            throw new NotFoundException("Фильм с ID " + newFilm.getId() + " не был найден!");
        }
        oldSettigs.setName(newFilm.getName());
        oldSettigs.setDescription(newFilm.getDescription());
        oldSettigs.setDuration(newFilm.getDuration());
        oldSettigs.setReleaseDate(newFilm.getReleaseDate());

        log.info("Фильма с ID={} успешно обновлен", newFilm.getId());
        return oldSettigs;
    }

    public void isValidFilm(Film film) {
        if (film.getDescription().length() > 200) {
            log.warn("Слишком длинное описание фильма: {}", film.getDescription());
            throw new DescriptionMaxLength("Максимальное описание описания - 200 символов!");
        }

        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.warn("Дата релиза фильма должна быть позже {}", LocalDate.of(1895, 12, 28));
            throw new ExceptionWithReleaseDate("Дата релиза — не раньше 28 декабря 1895 года!");
        }

        log.debug("Данные фильма введены корректно");
    }

    private Long getNextId() {
        long currentMax = films.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMax;
    }
}
