package pl.lotto.domain.numberreceiver.dto;

import lombok.Builder;

@Builder
public record TicketResponseDto(TicketDto ticket, String message) {
}
