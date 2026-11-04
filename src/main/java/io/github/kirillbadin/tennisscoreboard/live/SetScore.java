package io.github.kirillbadin.tennisscoreboard.live;

import java.util.Optional;

public class SetScore {

    // Вместо хранения отдельных полей для обычного гейма и тай-брейка,
        // лучше иметь коллекцию геймов/тай-брейков и наполнять её по ходу игры.
        // Это больше соответствовало бы реальному теннисному матчу.
        // А также позволило бы не хранить счёт в отдельных переменных, а вычислять его на лету.

    // Функциональной разницы нет, но наиболее распространённым порядком ключевых слов является: private static final
    private final static int WINNING_SET_GAMES = 6;
    private static final int TIEBREAK_GAMES = 6;

    private GameScore gameScore = new GameScore();
    private TieBreakScore tieBreakScore;
    private int firstGame; // Точнее firstGames
    private int secondGame; // Точнее secondGames


    public void addPoint(PlayerSide side) {

        // Можно вынести эту проверку во вспомогательный метод.
            // Тогда код в addPoint будет читаться более декларативно и будет сосредоточен только на самом счёте.
        if (isFinished()) {
            throw new IllegalStateException("Set already finished");
        }

        // Можно создать для гейма и тай-брейка общий интерфейс и хранить их объекты в поле по этому интерфейсу.
            // Это будет соответствовать принципу полиморфизма,
            // а также избавить от дублирования и упростит логику этого метода.
        if (tieBreakScore != null) {
            tieBreakScore.addPoint(side);
            tieBreakScore.getWinner().ifPresent(this::handleGameWin);
        } else {
            gameScore.addPoint(side);
            gameScore.getWinner().ifPresent(this::handleGameWin);
        }
    }

    // В других методах аргумент PlayerSide называется side, а здесь playerSide.
        // Для единообразия, лучше везде использовать одинаковые имена.
    // Метод выполняет два разных действия: увеличивает счёт и создаёт объект нового гейма.
        // Это нарушение Принципа единой ответственности (SRP) на уровне метода.
        // Стоит разделить эти две задачи на разные более специализированные методы.
    // Название handle* малоинформативное и часто свидетельствует о том, что метод делает слишком многое,
        // чтобы ясно выделить его ответственность и отразить её в названии.
    private void handleGameWin(PlayerSide winnerSide) {
        if (winnerSide == PlayerSide.FIRST) {
            firstGame++;
        } else {
            secondGame++;
        }

        if (firstGame == TIEBREAK_GAMES && secondGame == TIEBREAK_GAMES) {
            tieBreakScore = new TieBreakScore();
        } else {

            // TODO: После завершения сета метод всегда создаёт объект GameScore, который никогда не используется.
            gameScore = new GameScore();
        }
    }

    public Optional<PlayerSide> getWinner() {
        if (tieBreakScore != null) {
            return tieBreakScore.getWinner();
        }

        // Все магические числа лучше выносить в `private static final` константы с понятными именами.
            // Именованная константа делает код более семантически понятным.
        // Составные условия из if лучше выносить во вспомогательный метод с понятным названием
            // Сложные логические выражения требуют усилий для понимания, ухудшают читаемость кода и
            // увеличивают вероятность ошибки при их написании или изменении.
            // Лучше выносить такие условия в отдельный private-метод с понятным названием,
            // которое описывает бизнес-правило.
        if (firstGame >= WINNING_SET_GAMES && firstGame - secondGame >= 2) {
            return Optional.of(PlayerSide.FIRST);
        }
        if (secondGame >= WINNING_SET_GAMES && secondGame - firstGame >= 2) {
            return Optional.of(PlayerSide.SECOND);
        }
        return Optional.empty();
    }

    public boolean isFinished() {
        return getWinner().isPresent();
    }

    public int getGames(PlayerSide side) {
        return side == PlayerSide.FIRST ? firstGame : secondGame;
    }

    public GamePoint getGamePoint(PlayerSide side) {
        return gameScore.getScoreFor(side);
    }

    public Integer getTieBreakPoints(PlayerSide side) {
        if (tieBreakScore == null || tieBreakScore.getWinner().isPresent()) {
            return null;
        }
        return tieBreakScore.getPoints(side);
    }
}
