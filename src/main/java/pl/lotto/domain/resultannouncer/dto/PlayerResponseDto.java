package pl.lotto.domain.resultannouncer.dto;

import lombok.Builder;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

@Builder
public record PlayerResponseDto(PlayerDto player, TicketDto ticket) {
}
