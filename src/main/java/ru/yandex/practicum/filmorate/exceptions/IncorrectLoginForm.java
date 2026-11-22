package ru.yandex.practicum.filmorate.exceptions;

public class IncorrectLoginForm extends RuntimeException {
    public IncorrectLoginForm(String message) {
        super(message);
    }
}
