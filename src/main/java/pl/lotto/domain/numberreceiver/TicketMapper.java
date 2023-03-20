package pl.lotto.domain.numberreceiver;

import pl.lotto.domain.numberreceiver.dto.TicketDto;

class TicketMapper {
    static TicketDto mapToDto(Ticket ticket){
        return TicketDto.builder()
                .hash(ticket.hash())
                .numbersFromUser(ticket.numbersFromUser())
                .drawDate(ticket.drawDate())
                .build();
    }
}
