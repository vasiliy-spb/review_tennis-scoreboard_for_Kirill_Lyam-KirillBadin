package io.github.kirillbadin.tennisscoreboard.live;

import java.util.Optional;

public class TieBreakScore {

    // Нет метода isFinished(). Можно добавить его и использовать, как в соседних классах.

    private static final int WINNING_POINTS = 7;
    private static final int MINIMUM_DIFFERENCE = 2;

    private int firstSidePoints;
    private int secondSidePoints;

    // В других классах в методе addPoint аргумент называется side, а здесь playerSide.
        // Для единообразия, лучше везде использовать одинаковые имена.
    public void addPoint(PlayerSide playerSide) {

        // Можно вынести эту проверку во вспомогательный метод.
            // Тогда код в addPoint будет читаться более декларативно и будет сосредоточен только на самом счёте.
        if (getWinner().isPresent()) {
            throw new IllegalStateException("Tiebreak already finished");
        }
        if (playerSide == PlayerSide.FIRST) {
            firstSidePoints++;
        } else {
            secondSidePoints++;
        }
    }

    public Optional<PlayerSide> getWinner(){

        // Составные условия из if лучше выносить во вспомогательный метод с понятным названием
            // Сложные логические выражения требуют усилий для понимания, ухудшают читаемость кода и
            // увеличивают вероятность ошибки при их написании или изменении.
            // Лучше выносить такие условия в отдельный private-метод с понятным названием,
            // которое описывает бизнес-правило.
        if (firstSidePoints >= WINNING_POINTS && firstSidePoints - secondSidePoints >= MINIMUM_DIFFERENCE) {
            return Optional.of(PlayerSide.FIRST);
        }
        if (secondSidePoints >= WINNING_POINTS && secondSidePoints - firstSidePoints >= MINIMUM_DIFFERENCE) {
            return Optional.of(PlayerSide.SECOND);
        }
        return Optional.empty();
    }

    public int getPoints(PlayerSide side) {
        return side == PlayerSide.FIRST ? firstSidePoints : secondSidePoints;
    }
}
