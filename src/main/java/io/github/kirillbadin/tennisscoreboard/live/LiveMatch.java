package io.github.kirillbadin.tennisscoreboard.live;

import io.github.kirillbadin.tennisscoreboard.exceptions.PlayerNotInMatchException;
import lombok.Getter;

import java.util.Optional;
import java.util.UUID;

public class LiveMatch {

    // Можно добавить в этот класс метод isFinished, чтобы при необходимости вызывать его,
        // а не liveMatch.getWinnerSide().isEmpty().

    @Getter
    private final UUID uuid;
    private final String firstPlayerName;
    private final String secondPlayerName;
    private final MatchScore matchScore;

    public LiveMatch(String firstPlayerName, String secondPlayerName) {
        this.firstPlayerName = firstPlayerName;
        this.secondPlayerName = secondPlayerName;

        // Класс текущего матча в этом проекте может не хранить в себе ID.
            // Но если ID в нём есть, то можно получать его извне (по аналогии с JPA Entity),
            // а не генерировать в самом классе модели.
        this.uuid = UUID.randomUUID();
        this.matchScore = new MatchScore();
    }

    public void addPoint(String name) {

        // TODO: Нет проверки на то, что матч не завершён.
            // Попытка начислить очко в уже завершённом матче — это не нормальная ситуация и должна приводить к исключению.
            // Проверка есть ниже по стеку, но каждый класс сам должен защищать свои инварианты.

        // Можно ввести вспомогательный метод и избавиться от блоков if. В таком духе:
        /*
        PlayerSide side = getSideFor(name);
        matchScore.addPoint(side);
         */
        if (firstPlayerName.equals(name)) {
            matchScore.addPoint(PlayerSide.FIRST);
            return;
        }
        if (secondPlayerName.equals(name)) {
            matchScore.addPoint(PlayerSide.SECOND);
            return;
        }

        // Валидацию имени игрока лучше проводить в самом начале метода.
            // Это лучше читается и соответствует принципу Fail Fast по стилю написания.
        throw new PlayerNotInMatchException("Игрок '" + name + "' не найден в матче");
    }

    public Optional<String> getWinnerName() {
        return matchScore.getWinner().map(this::getPlayerName);
    }

    public Optional<PlayerSide> getWinnerSide() {
        return matchScore.getWinner();
    }

    public String getPlayerName(PlayerSide side) {
        return side == PlayerSide.FIRST ? firstPlayerName : secondPlayerName;
    }

    public int getSets(PlayerSide side) {
        return matchScore.getSets(side);
    }

    public int getGames(PlayerSide side) {
        return matchScore.getGames(side);
    }

    public GamePoint getGamePoint(PlayerSide side) {
        return matchScore.getGamePoint(side);
    }

    public Integer getTieBreakPoints(PlayerSide side) {
        return matchScore.getTieBreakPoints(side);
    }
}