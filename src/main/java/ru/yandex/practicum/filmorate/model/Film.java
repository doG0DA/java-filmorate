package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Film.
 */
@Getter
@Setter
public class Film {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private String description;

    private LocalDate releaseDate;

    @Positive
    private int duration;
}
