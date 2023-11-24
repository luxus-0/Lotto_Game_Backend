package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numberreceiver.dto.TicketResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class NumberReceiverFacade {

    private final NumbersReceiverValidator numberValidator;
    private final DrawDateFacade drawDateFacade;
    private final TicketRepository ticketRepository;
    private final TicketIdGenerator hashGenerator;
    private final TicketValidationMessageProvider validationMessage;

    public TicketResponseDto inputNumbers(Set<Integer> numbersFromUser) {
        boolean validate = numberValidator.validate(numbersFromUser);
        if (validate) {
            String ticketId = hashGenerator.generateTicketId();
            LocalDateTime drawDate = drawDateFacade.retrieveNextDrawDate();
            Ticket ticketSaved = ticketRepository.save(new Ticket(ticketId, numbersFromUser, drawDate));
            TicketCreator ticket = new TicketCreator(validationMessage);
            return ticket.createTicketSaved(ticketSaved);
        }
        return TicketResponseDto.builder()
                .message(validationMessage.getMessage())
                .build();
    }

    public List<TicketDto> retrieveAllTicketByDrawDate(LocalDateTime date) {
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        if (date.isAfter(nextDrawDate)) {
            throw new IllegalArgumentException("Date is after next draw date");
        }
        return ticketRepository.findTicketsByDrawDate(date)
                .stream()
                .map(TicketMapper::mapToTicketDto)
                .toList();
    }
}