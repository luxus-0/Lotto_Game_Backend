package pl.lotto.domain.resultchecker.dto;

import lombok.Builder;
import pl.lotto.domain.resultannouncer.ResultLotto;
import pl.lotto.domain.resultchecker.Ticket;

import java.util.List;

@Builder
public record PlayersDto(String message, List<Ticket> tickets, List<ResultLotto> results) {
}
