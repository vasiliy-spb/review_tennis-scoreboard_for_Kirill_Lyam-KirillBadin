package io.github.kirillbadin.tennisscoreboard.live;

import java.util.Optional;

public class GameScore {

    // Нет метода isFinished(). Можно добавить его и использовать, как в соседних классах.

    private GamePoint firstSidePoints = GamePoint.LOVE;
    private GamePoint secondSidePoints = GamePoint.LOVE;
    private PlayerSide gameWinner; // Можно просто winner

    public void addPoint(PlayerSide side){

        // Можно вынести эту проверку во вспомогательный метод.
            // Тогда код в addPoint будет читаться более декларативно и будет сосредоточен только на самом счёте.
        if(gameWinner != null){
            throw new IllegalStateException("Game already finished");
        }

        GamePoint winnerPoints = getScoreFor(side);
        GamePoint opponentPoints = getScoreFor(side.opposite());

        if (winnerPoints == GamePoint.ADVANTAGE) {
            gameWinner = side;
        } else if (opponentPoints == GamePoint.ADVANTAGE) {
            setScoreFor(side.opposite(), GamePoint.FORTY);
        } else if (winnerPoints == GamePoint.FORTY) {
            if (opponentPoints == GamePoint.FORTY) {
                setScoreFor(side, GamePoint.ADVANTAGE);
            } else {
                gameWinner = side;
            }
        } else {
            setScoreFor(side, winnerPoints.next());
        }
    }

    public Optional<PlayerSide> getWinner() {
        return Optional.ofNullable(gameWinner);
    }

    GamePoint getScoreFor(PlayerSide side) {
        return side == PlayerSide.FIRST ? firstSidePoints : secondSidePoints;
    }

    private void setScoreFor(PlayerSide side, GamePoint points) {

        // Тело блоков if-else всегда следует оборачивать в {}. Это:
            // - Предотвращает критические баги при поддержке кода —
                // если кто-то добавит перед существующим кодом новую строку (например логирование)
            // - Позволяет избежать ловушки "Блуждающего else" (Dangling else)
            // - Повышает скорость чтения кода —
                // фигурные скобки служат понятными визуальными якорями, разграничивающими блоки кода
        if (side == PlayerSide.FIRST) firstSidePoints = points;
        else secondSidePoints = points;
    }
}
