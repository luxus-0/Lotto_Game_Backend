package pl.lotto.domain.resultchecker.dto;

import lombok.Builder;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

import java.util.List;
import java.util.Set;

@Builder
public record PlayerWinnerResponseDto(PlayerWinnerDto playerWinner, TicketDto ticket) {
}
