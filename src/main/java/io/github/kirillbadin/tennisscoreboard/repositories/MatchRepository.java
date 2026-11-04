package io.github.kirillbadin.tennisscoreboard.repositories;

import io.github.kirillbadin.tennisscoreboard.entity.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MatchRepository extends JpaRepository<Match, Long> {

    // Лучше выносить тексты JPQL запросов в константы и давать им понятные имена.

    // Для визуального разделения запросов на строки лучше использовать текстовые блоки, а не конкатенацию строк.

    @EntityGraph(attributePaths = {"firstPlayer", "secondPlayer", "winner"})
    Page<Match> findAll(Pageable pageable);

    // Для выполнения регистронезависимого поиска по шаблону в PostgreSQL можно использовать оператор ILIKE.
    // TODO: Wildcard Injection в параметре playerName.
        // В SQL в шаблоне LIKE:
        // '%' соответствует любой последовательности символов (включая пустую).
        // '_' соответствует ровно одному любому символу.
        // Сейчас, если пользователь например в качестве параметра playerName передаст '%' или '_',
        // то приложение вернёт ему список вообще всех матчей.
        // В текущем проекте это безобидное поведение,
        // но уязвимость Wildcard Injection может становиться вектором для атаки.
        // Стоит найти решение, как это исправить.
    @EntityGraph(attributePaths = {"firstPlayer", "secondPlayer", "winner"})
    @Query("SELECT m FROM Match m " +
            "WHERE LOWER(m.firstPlayer.name) LIKE LOWER(CONCAT('%', :playerName, '%')) " +
            "OR LOWER(m.secondPlayer.name) LIKE LOWER(CONCAT('%', :playerName, '%')) ")
    Page<Match> findAllByPlayerName(@Param("playerName") String playerName, Pageable pageable);
}
