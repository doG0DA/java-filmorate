package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.debug("Запрос на вывод всех пользователей");
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User newUser) {
        log.debug("Запрос на добавление пользователя {}", newUser);

        isValidUser(newUser);

        newUser.setId(getNextId());

        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }

        users.put(newUser.getId(), newUser);

        log.info("Пользователь {} успешно добавлен", newUser.getName());
        return newUser;
    }

    @PutMapping
    public User changeUser(@Valid @RequestBody User newUser) {
        log.debug("Запрос на обновление данных пользователя {}", newUser);

        if (newUser.getId() == null || newUser.getId() <= 0) {
            log.warn("Не указан ID обновленного пользователя");
            throw new ConditionsNotMetException("ID должен быть указан!");
        }

        isValidUser(newUser);

        User oldUser = users.get(newUser.getId());
        if (oldUser == null) {
            log.warn("Невозможно произвести обновление, пользователь c ID={} не был найден", newUser.getId());
            throw new NotFoundException("Фильм с ID " + newUser.getId() + " не был найден!");
        }

        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());

        if (newUser.getName() == null || newUser.getName().isBlank()) {
            oldUser.setName(newUser.getLogin());
        } else {
            oldUser.setName(newUser.getName());
        }

        oldUser.setBirthday(newUser.getBirthday());

        log.info("Пользователь с ID={} успешно обновлен", oldUser.getId());
        return oldUser;
    }

    public void isValidUser(User user) {
        if (user.getLogin() != null && user.getLogin().contains(" ")) {
            log.warn("Неверный формат Login пользователя");
            throw new IncorrectLoginForm("Логин не может быть пустым и содержать пробелы");
        }

        log.debug("Данные пользователя введены корректно");
    }

    private Long getNextId() {
        long currentMax = users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMax;
    }
}
