package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numberreceiver.dto.TicketResponseDto;

@Service
@AllArgsConstructor
public class TicketCreator {
    private final TicketValidationMessageProvider ticketValidationMessage;

    public TicketResponseDto createTicketSaved(Ticket ticketSaved) {
        return TicketResponseDto.builder()
                .ticketDto(TicketDto.builder()
                        .ticketId(ticketSaved.ticketId())
                        .numbers(ticketSaved.numbers())
                        .drawDate(ticketSaved.drawDate())
                        .build())
                .message(ticketValidationMessage.getMessage())
                .build();
    }
}
