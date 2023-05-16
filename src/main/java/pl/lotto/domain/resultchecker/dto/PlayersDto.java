package pl.lotto.domain.resultchecker.dto;

import lombok.Builder;
import pl.lotto.domain.resultannouncer.ResultLotto;

import java.util.List;

@Builder
public record PlayersDto(List<ResultLotto> results) {
}
