package pl.lotto.domain.resultchecker;

import pl.lotto.domain.resultannouncer.ResultLotto;
import pl.lotto.domain.resultchecker.dto.PlayersDto;
import pl.lotto.domain.resultchecker.exceptions.PlayerResultNotFoundException;

import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.PLAYER_NOT_FOUND;

public class ResultCheckerFacadeTestMessageProvider {
    public static String getPlayerResultMessage(PlayersDto playersDto) {
        return playersDto.results().stream()
                .map(ResultLotto::message)
                .findAny().orElseThrow(() -> new PlayerResultNotFoundException(PLAYER_NOT_FOUND));
    }
}
