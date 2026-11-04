package io.github.kirillbadin.tennisscoreboard.live;

public enum GamePoint {
    LOVE("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40"),
    ADVANTAGE("AD");

    // В идеале класс, относящийся к слою доменной модели, не должен знать о том, как он будет отображаться во View.
        // Поэтому этот enum не должен содержать поле с названием display.
        // На мой взгляд, конкретно в этом проекте допустим компромисс в виде хранения строкового значения GamePoint,
        // но только не с семантикой "значение для отображения", а со смыслом "строковое значение по умолчанию".
        // Поэтому полю больше подошло бы название stringValue или defaultStringValue.
    private final String display;

    GamePoint(String display) {
        this.display = display;
    }

    // Можно использовать lombok.Getter
    public String getDisplay() {
        return display;
    }

    public GamePoint next() {
        if (this == ADVANTAGE) {
            throw new IllegalStateException("ADVANTAGE has no next value");
        }
        return values()[ordinal() + 1];
    }
}
