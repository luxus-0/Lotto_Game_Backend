package pl.lotto.domain.numberreceiver;

import pl.lotto.domain.numberreceiver.dto.TicketDto;

class TicketMapper {
    static TicketDto mapToTicketDto(Ticket ticket) {
        return TicketDto.builder()
                .ticketId(ticket.ticketId())
                .numbers(ticket.numbersFromUser())
                .drawDate(ticket.drawDate())
                .build();
    }
}
