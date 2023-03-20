package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.lotto.domain.numberreceiver.*;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResultDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Log4j2
public class NumberReceiverFacade {

    private final NumbersReceiverValidator numberValidator;
    private final DateTimeDrawGenerator dateTimeDrawGenerator;
    private final TicketRepository ticketRepository;
    private final HashGenerable hashGenerator;


    public NumberReceiverResultDto inputNumbers(Set<Integer> numbersFromUser) {
        boolean validate = numberValidator.validate(numbersFromUser);
        if (!validate) {
           String ticketId = hashGenerator.getHash();
           LocalDateTime drawDate = dateTimeDrawGenerator.generateNextDrawDate();
           Ticket ticketSaved = ticketRepository.save(new Ticket(ticketId, numbersFromUser, drawDate));
            return NumberReceiverResultDto.builder()
                    .ticketDto(new TicketDto(ticketSaved.hash(), ticketSaved.numbersFromUser(), ticketSaved.drawDate()))
                    .message(createResultMessage())
                    .build();
        }
        return NumberReceiverResultDto.builder()
                .message(createResultMessage())
                .build();
    }

    private String createResultMessage() {
        List<ValidationResult> messagesResult = numberValidator.errors;
        return messagesResult.stream()
                .map(ValidationResult::getInfo)
                .collect(Collectors.joining(","));
    }


    public List<TicketDto> retrieveAllTicketByDrawDate(LocalDateTime drawDate){
        LocalDateTime nextDrawDate = dateTimeDrawGenerator.generateNextDrawDate();
        if(drawDate.isAfter(nextDrawDate)) {
            return Collections.emptyList();
        }
            List<Ticket> allTicketByDrawDate = ticketRepository.findAllTicketsByDrawDate(drawDate);
            return allTicketByDrawDate.stream()
                    .map(TicketMapper::mapToDto)
                    .toList();
    }

    public TicketDto findByHash(String hash){
        Ticket ticket = ticketRepository.findByHash(hash);
        return TicketDto.builder()
                .hash(ticket.hash())
                .numbersFromUser(ticket.numbersFromUser())
                .drawDate(ticket.drawDate())
                .build();
    }
}