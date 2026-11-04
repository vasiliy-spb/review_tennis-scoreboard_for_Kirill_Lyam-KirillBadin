# Роадмап рефакторинга по файлам

Это упорядоченный список файлов, которые следует исправлять в соответствии с замечаниями в комментариях. Рекомендую двигаться последовательно.

Файлы, не указанные в списке, можно исправлять в любом порядке.

### Шаг 1: Entity и слой доступа к данным

- `/entity/Match.java`
- `/repositories/MatchRepository.java`

### Шаг 2: Доменные модели

- `/live/GamePoint.java`
- `/live/GameScore.java`
- `/live/TieBreakScore.java`
- `/live/SetScore.java`
- `/live/MatchScore.java`
- `/live/LiveMatch.java`

### Шаг 3: Сервисный слой

- `/live/LiveMatchStorage.java`
- `/services/LiveMatchService.java`
- `/services/MatchService.java`

### Шаг 4: Контроллер

- `/controllers/MatchController.java`

### Шаг 5: Конфигурация, мапперы, валидаторы, обработка исключений

- `/exceptions/GlobalExceptionHandler.java`
- `/exceptions/PlayerNotInMatchException.java`
