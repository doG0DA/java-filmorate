package ru.yandex.practicum.filmorate.exceptions;

public class IncorrectEmailForm extends RuntimeException {
    public IncorrectEmailForm(String message) {
        super(message);
    }
}
