package io.github.kirillbadin.tennisscoreboard.services;

import io.github.kirillbadin.tennisscoreboard.dto.MatchDto;
import io.github.kirillbadin.tennisscoreboard.dto.MatchesPageDto;
import io.github.kirillbadin.tennisscoreboard.entity.Match;
import io.github.kirillbadin.tennisscoreboard.entity.Player;
import io.github.kirillbadin.tennisscoreboard.live.LiveMatch;
import io.github.kirillbadin.tennisscoreboard.live.PlayerSide;
import io.github.kirillbadin.tennisscoreboard.repositories.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchService {

    // TODO: Класс совмещает несколько разных ответственностей:
        // - работает с завершёнными матчами
        // - занимается преобразованием в DTO
        //
        // Это нарушает принцип единой ответственности (SRP).
        // Стоит вынести логику преобразования в отдельный компонент — маппер.

    private final MatchRepository matchRepository;
    private final PlayerService playerService;

    @Transactional(readOnly = true)
    public MatchesPageDto getMatchesPage(Pageable pageable, String playerName) {
        Page<Match> allMatches = (playerName == null || playerName.isBlank())
                ? matchRepository.findAll(pageable)
                : matchRepository.findAllByPlayerName(playerName, pageable);

        // Логику преобразования Entity -> DTO лучше вынести во вспомогательный метод.
            // Ещё лучше в отдельный компонент — маппер.
        Page<MatchDto> dtoPage = allMatches.map(match -> new MatchDto(
                                                        match.getFirstPlayer().getName(),
                                                        match.getSecondPlayer().getName(),
                                                        match.getWinner().getName()
        ));
        return new MatchesPageDto(
                dtoPage.getContent(),
                dtoPage.getNumber(),
                dtoPage.getTotalPages());
    }
    @Transactional
    public void save(LiveMatch liveMatch) {

        // TODO: По ТЗ и логике текущей реализации, игроки сохраняются в БД при создании матча, а не после его завершения.
            // Поэтому если в этом методе игрока по какой-то причине не существует в БД —
            // это неожиданная ситуация и должна приводить к исключению.
        // Код с промежуточными переменными с понятными именами читается лучше,
            // чем вызовы методов, передаваемые в аргументы другим методам.
        Player firstPlayer = playerService.getOrCreatePlayer(liveMatch.getPlayerName(PlayerSide.FIRST));
        Player secondPlayer = playerService.getOrCreatePlayer(liveMatch.getPlayerName(PlayerSide.SECOND));
        PlayerSide winnerSide = liveMatch.getWinnerSide().orElseThrow(() -> new IllegalStateException(
                "Нельзя сохранить незавершённый матч: победитель не определён, uuid="
                        + liveMatch.getUuid()));
        Player winner = (winnerSide == PlayerSide.FIRST) ? firstPlayer : secondPlayer;
        matchRepository.save(new Match(firstPlayer, secondPlayer, winner));
    }
}
