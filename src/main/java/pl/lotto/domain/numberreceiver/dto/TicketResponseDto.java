package pl.lotto.domain.numberreceiver.dto;

import lombok.Builder;

@Builder
public record TicketResponseDto(TicketDto ticketDto, TicketMessageDto message) {
}
