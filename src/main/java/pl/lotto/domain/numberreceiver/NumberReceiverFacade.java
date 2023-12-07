package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numberreceiver.dto.TicketResponseDto;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static pl.lotto.domain.drawdate.DrawDateMessageProvider.INCORRECT_NEXT_DRAW_DATE;
import static pl.lotto.domain.numberreceiver.NumbersReceiverValidator.getInputNumbersValidationMessage;
import static pl.lotto.domain.numberreceiver.TicketMapper.mapToTicketResponseDto;

@AllArgsConstructor
@Service
public class NumberReceiverFacade {

    private final NumbersReceiverValidator validator;
    private final DrawDateFacade drawDateFacade;
    private final TicketRepository ticketRepository;
    private final TicketUUIDGenerator ticketUUIDGenerator;

    public TicketResponseDto inputNumbers(Set<Integer> numbersFromUser) {
        boolean validate = validator.validate(numbersFromUser);
        if (validate) {
            String ticketUUID = ticketUUIDGenerator.generateTicketUUID();
            LocalDateTime drawDate = drawDateFacade.retrieveNextDrawDate();
            Ticket ticket = new Ticket(ticketUUID, numbersFromUser, drawDate, getInputNumbersValidationMessage());
            Ticket ticketSaved = ticketRepository.save(ticket);

            return mapToTicketResponseDto(ticketSaved);
        }
        return TicketResponseDto.builder()
                .message(getInputNumbersValidationMessage().getInfo())
                .build();
    }

    public Set<Integer> retrieveUserNumbersByDrawDate(LocalDateTime nextDrawDate) {
        return retrieveAllTicketByDrawDate(nextDrawDate).stream()
                .map(TicketDto::numbers)
                .findAny()
                .orElse(Collections.emptySet());
    }

    public List<TicketDto> retrieveAllTicketByDrawDate(LocalDateTime date) {
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        if (date.isAfter(nextDrawDate)) {
            throw new DateTimeException(INCORRECT_NEXT_DRAW_DATE);
        }
        return ticketRepository.findTicketsByDrawDate(date)
                .stream()
                .map(TicketMapper::mapToTicketDto)
                .toList();
    }
}