package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numberreceiver.dto.InputNumbersResponseDto;

import static pl.lotto.domain.numberreceiver.NumbersReceiverValidator.retrieveInputNumbersValidationMessage;

@AllArgsConstructor
class TicketMapper {
    public static TicketDto mapToTicketDto(Ticket ticket) {
        return TicketDto.builder()
                .ticketUUID(ticket.ticketUUID())
                .inputNumbers(ticket.inputNumbers())
                .drawDate(ticket.drawDate())
                .build();
    }
}
