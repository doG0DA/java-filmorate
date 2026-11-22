package ru.yandex.practicum.filmorate.exceptions;

public class DescriptionMaxLength extends RuntimeException {
    public DescriptionMaxLength(String message) {
        super(message);
    }
}
