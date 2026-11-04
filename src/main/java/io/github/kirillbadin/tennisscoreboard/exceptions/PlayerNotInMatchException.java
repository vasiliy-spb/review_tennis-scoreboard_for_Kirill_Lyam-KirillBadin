package io.github.kirillbadin.tennisscoreboard.exceptions;

public class PlayerNotInMatchException extends RuntimeException {

    // TODO: Сейчас PlayerNotInMatchException уходит в 500, хотя это клиентская ошибка (400).

    public PlayerNotInMatchException(String message) {
        super(message);
    }
}
