package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

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
