package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.DescriptionMaxLength;
import ru.yandex.practicum.filmorate.exceptions.ExceptionWithReleaseDate;
import ru.yandex.practicum.filmorate.exceptions.IncorrectLoginForm;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmorateApplicationTests {

    private FilmController filmController;
    private UserController userController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        userController = new UserController();
    }

    @Test
    void contextLoads() {
    }

    private Film validFilm() {
        Film film = new Film();
        film.setName("Матрица");
        film.setDescription("Фантастический боевик");
        film.setReleaseDate(LocalDate.of(1999, 3, 31));
        film.setDuration(136);
        return film;
    }

    private User validUser() {
        User user = new User();
        user.setEmail("ivan@mail.ru");
        user.setLogin("ivan");
        user.setName("Иван");
        user.setBirthday(LocalDate.of(1995, 1, 1));
        return user;
    }

    @Test
    void validFilmPassesValidation() {
        assertDoesNotThrow(() -> filmController.isValidFilm(validFilm()));
    }

    @Test
    void descriptionOf200CharsPasses() {
        Film film = validFilm();
        film.setDescription("a".repeat(200));

        assertDoesNotThrow(() -> filmController.isValidFilm(film));
    }

    @Test
    void descriptionOf201CharsThrows() {
        Film film = validFilm();
        film.setDescription("a".repeat(201));

        assertThrows(DescriptionMaxLength.class, () -> filmController.isValidFilm(film));
    }

    @Test
    void releaseDateOnBorderPasses() {
        Film film = validFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 28));

        assertDoesNotThrow(() -> filmController.isValidFilm(film));
    }

    @Test
    void releaseDateBeforeBorderThrows() {
        Film film = validFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        assertThrows(ExceptionWithReleaseDate.class, () -> filmController.isValidFilm(film));
    }

    @Test
    void nullReleaseDateThrows() {
        Film film = validFilm();
        film.setReleaseDate(null);

        assertThrows(ExceptionWithReleaseDate.class, () -> filmController.isValidFilm(film));
    }

    @Test
    void validUserPassesValidation() {
        assertDoesNotThrow(() -> userController.isValidUser(validUser()));
    }

    @Test
    void loginWithSpaceThrows() {
        User user = validUser();
        user.setLogin("ivan petrov");

        assertThrows(IncorrectLoginForm.class, () -> userController.isValidUser(user));
    }

    @Test
    void loginWithSpaceAtEndThrows() {
        User user = validUser();
        user.setLogin("ivan ");

        assertThrows(IncorrectLoginForm.class, () -> userController.isValidUser(user));
    }
}