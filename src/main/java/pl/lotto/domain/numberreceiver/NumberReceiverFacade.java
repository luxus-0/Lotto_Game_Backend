package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.dto.InputNumbersRequestDto;
import pl.lotto.domain.numberreceiver.dto.InputNumbersResponseDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numberreceiver.exceptions.InputNumbersNotFoundException;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static pl.lotto.domain.drawdate.DrawDateMessageProvider.INCORRECT_NEXT_DRAW_DATE;

@AllArgsConstructor
@Service
public class NumberReceiverFacade {

    private final NumbersReceiverValidator validator;
    private final DrawDateFacade drawDateFacade;
    private final TicketRepository ticketRepository;
    private final TicketUUIDGenerator ticketUUIDGenerator;

    public InputNumbersResponseDto inputNumbers(InputNumbersRequestDto inputNumbersRequest) {
        Set<Integer> inputNumbers = inputNumbersRequest.inputNumbers();
        boolean validate = validator.validate(inputNumbers);
        if (validate) {
            String ticketUUID = ticketUUIDGenerator.generateTicketUUID();
            LocalDateTime drawDate = drawDateFacade.retrieveNextDrawDate();
            Ticket ticket = new Ticket(ticketUUID, inputNumbers, drawDate, validator.getMessage());
            Ticket ticketSaved = ticketRepository.save(ticket);

            return InputNumbersResponseDto.builder()
                    .ticketUUID(ticketSaved.ticketUUID())
                    .drawDate(ticketSaved.drawDate())
                    .inputNumbers(ticketSaved.inputNumbers())
                    .message(ticketSaved.message())
                    .build();
        }
        throw new InputNumbersNotFoundException("InputNumbers not found");
    }

    public Set<Integer> retrieveInputNumbersByDrawDate(LocalDateTime nextDrawDate) {
        return retrieveTicketsByDrawDate(nextDrawDate).stream()
                .map(TicketDto::inputNumbers)
                .findAny()
                .orElseThrow(InputNumbersNotFoundException::new);
    }

    public List<TicketDto> retrieveTicketsByDrawDate(LocalDateTime date) {
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