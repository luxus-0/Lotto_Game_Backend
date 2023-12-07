package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numberreceiver.dto.TicketResponseDto;

import static pl.lotto.domain.numberreceiver.NumbersReceiverValidator.retrieveInputNumbersValidationMessage;

@AllArgsConstructor
class TicketMapper {
    public static TicketDto mapToTicketDto(Ticket ticket) {
        return TicketDto.builder()
                .ticketId(ticket.ticketId())
                .numbers(ticket.numbers())
                .drawDate(ticket.drawDate())
                .build();
    }

    public static TicketResponseDto mapToTicketResponseDto(Ticket ticketSaved) {
        return TicketResponseDto.builder()
                .ticket(TicketDto.builder()
                        .ticketId(ticketSaved.ticketId())
                        .numbers(ticketSaved.numbers())
                        .drawDate(ticketSaved.drawDate())
                        .build())
                .message(retrieveInputNumbersValidationMessage().getInfo())
                .build();
    }
}
