package ru.yandex.practicum.filmorate.exceptions;

public class ExceptionWithReleaseDate extends RuntimeException {
    public ExceptionWithReleaseDate(String message) {
        super(message);
    }
}
