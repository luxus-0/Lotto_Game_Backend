package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.dto.InputNumbersRequestDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numberreceiver.dto.TicketResponseDto;
import pl.lotto.domain.resultchecker.exceptions.TicketNotSavedException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Log4j2
public class NumberReceiverFacade {

    private final NumbersReceiverValidator validator;
    private final DrawDateFacade drawDateFacade;
    private final TicketRepository ticketRepository;
    private final TicketUUIDGenerator ticketUUIDGenerator;

    @Cacheable("inputNumbers")
    public TicketResponseDto inputNumbers(InputNumbersRequestDto inputNumbersRequest) throws Exception {
        Set<Integer> inputNumbers = inputNumbersRequest.inputNumbers();
        boolean validate = validator.validate(inputNumbers);
        if (validate) {
            String ticketUUID = ticketUUIDGenerator.generateTicketUUID();
            LocalDateTime drawDate = drawDateFacade.retrieveNextDrawDate();
            Ticket ticket = new Ticket(ticketUUID, inputNumbers, drawDate, validator.getMessage());
            Ticket ticketSaved = ticketRepository.save(ticket);
            checkSavedTicket(ticketSaved);
            log.info("Ticket saved to database: " +ticketSaved);
            return TicketResponseDto.builder()
                    .ticketUUID(ticketSaved.ticketUUID())
                    .drawDate(ticketSaved.drawDate())
                    .inputNumbers(ticketSaved.inputNumbers())
                    .message(ticketSaved.message())
                    .build();
        }
        return TicketResponseDto.builder()
                .ticketUUID("")
                .inputNumbers(Collections.emptySet())
                .message("Ticket not found")
                .build();
    }

    private static void checkSavedTicket(Ticket ticketSaved) throws TicketNotSavedException {
        if(ticketSaved.ticketUUID() == null || ticketSaved.inputNumbers() == null || ticketSaved.drawDate() == null || ticketSaved.message() == null){
            throw new TicketNotSavedException();
        }
    }

    public Set<Integer> retrieveInputNumbersByDrawDate(LocalDateTime nextDrawDate) {
        return retrieveTicketsByDrawDate(nextDrawDate).stream()
                .map(TicketDto::inputNumbers)
                .findAny()
                .orElse(Collections.emptySet());
    }

    public List<TicketDto> retrieveTicketsByDrawDate(LocalDateTime date) {
            return ticketRepository.findTicketsByDrawDate(date)
                    .stream()
                    .map(TicketMapper::mapToTicketDto)
                    .toList();
        }
}