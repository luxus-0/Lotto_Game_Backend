package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.dto.TicketResultDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class NumberReceiverFacade {

    private final NumbersReceiverValidator numberValidator;
    private final DrawDateFacade drawDateFacade;
    private final TicketRepository ticketRepository;
    private final HashGenerable hashGenerator;

    public TicketResultDto inputNumbers(Set<Integer> numbersFromUser) {
        boolean validate = numberValidator.validate(numbersFromUser);
        if (validate) {
            String ticketId = hashGenerator.getHash();
            LocalDateTime drawDate = drawDateFacade.retrieveNextDrawDate();
            Ticket ticketSaved = ticketRepository.save(new Ticket(ticketId, numbersFromUser, drawDate));
            return TicketResultDto.builder()
                    .ticketDto(TicketDto.builder()
                            .hash(ticketSaved.hash())
                            .numbers(ticketSaved.numbersFromUser())
                            .drawDate(ticketSaved.drawDate())
                            .build())
                    .message(createResultMessage())
                    .build();
        }
        return TicketResultDto.builder()
                .ticketDto(null)
                .message(createResultMessage())
                .build();
    }

    private String createResultMessage() {
        List<ValidationResult> messagesResult = numberValidator.errors;
        return messagesResult.stream()
                .map(ValidationResult::getInfo)
                .collect(Collectors.joining(","));
    }

    public List<TicketDto> retrieveAllTicketByDrawDate(LocalDateTime date) {
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        if (date.isAfter(nextDrawDate)) {
            return Collections.emptyList();
        }
        return ticketRepository.findAllByDrawDate(date)
                .stream()
                .filter(ticket -> ticket.drawDate().isEqual(date))
                .map(TicketMapper::mapToTicketDto)
                .toList();
    }
}