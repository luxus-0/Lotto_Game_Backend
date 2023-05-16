package pl.lotto.domain.numberreceiver.dto;

import lombok.Builder;

@Builder
public record TicketResultDto(TicketDto ticketDto, String message) {
}
