package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResultDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class NumberReceiverFacade {

    private final NumbersReceiverValidator numberValidator;

    private final DateTimeDrawGenerator dateTimeDrawGenerator;

    private final TicketRepository ticketRepository;
    private final HashGenerable hashGenerator;


    public NumberReceiverResultDto inputNumbers(Set<Integer> numbersFromUser) {
        boolean validate = numberValidator.validate(numbersFromUser);
        if (validate) {
           String ticketId = hashGenerator.getHash();
           LocalDateTime drawDate = dateTimeDrawGenerator.generateNextDrawDate();
           Ticket ticketSaved = ticketRepository.save(new Ticket(ticketId, numbersFromUser, drawDate));
            return getNumberReceiverResultDto(ticketSaved);
        }
        return getReceiverResultDto();
    }
    private NumberReceiverResultDto getReceiverResultDto() {
        return NumberReceiverResultDto.builder()
                .message(createResultMessage())
                .build();
    }

    private NumberReceiverResultDto getNumberReceiverResultDto(Ticket ticketSaved) {
        return NumberReceiverResultDto.builder()
                .ticketDto(new TicketDto(ticketSaved.hash(), ticketSaved.numbersFromUser(), ticketSaved.drawDate()))
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
        if (drawDate.isAfter(nextDrawDate)) {
            return Collections.emptyList();
        }
        return ticketRepository.findAllByDrawDate(drawDate)
                .stream()
                .filter(ticket -> ticket.drawDate().isEqual(drawDate))
                .map(TicketMapper::mapToTicketDto)
                .toList();
    }

    LocalDateTime createDrawDateForTicket(){
        return dateTimeDrawGenerator.generateNextDrawDate();
    }

    public TicketDto findByHash(String hash) {
        Ticket ticket = ticketRepository.findByHash(hash);
        return TicketDto.builder()
                .hash(ticket.hash())
                .numbersFromUser(ticket.numbersFromUser())
                .drawDate(ticket.drawDate())
                .build();
    }
}