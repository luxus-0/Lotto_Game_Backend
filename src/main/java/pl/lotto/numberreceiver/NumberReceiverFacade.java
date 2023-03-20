package pl.lotto.numberreceiver;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.lotto.datetimegenerator.DateTimeDrawFacade;
import pl.lotto.numberreceiver.dto.NumberResultDto;
import pl.lotto.numberreceiver.dto.TicketDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Log4j2
public class NumberReceiverFacade {

    private final NumbersReceiverValidator numberValidator;
    private final DateTimeDrawFacade dateTimeDrawFacade;
    private final TicketRepository ticketRepository;
    private final HashGenerator hashGenerator;


    public NumberResultDto inputNumbers(Set<Integer> numbersFromUser) {
        boolean validate = numberValidator.validate(numbersFromUser);
        if (!validate) {
           String ticketId = hashGenerator.getHash();
           LocalDateTime drawDate = dateTimeDrawFacade.readNextDrawDate();
           Ticket ticketSaved = ticketRepository.save(new Ticket(ticketId, numbersFromUser, drawDate));
           String message = getResultMessage();
            return NumberResultDto.builder()
                    .ticketId(ticketSaved.hash())
                    .drawDate(ticketSaved.drawDate())
                    .numbersFromUser(numbersFromUser)
                    .message(message)
                    .build();
        }
        return NumberResultDto.builder()
                .message(getResultMessage())
                .build();
    }

    private String getResultMessage() {
        List<String> messagesResult = numberValidator.messages;
        return messagesResult.stream()
                .findAny()
                .orElse("not found validation message");
    }


    public List<TicketDto> retrieveAllTicketByDrawDate(LocalDateTime dateTime){
        LocalDateTime nextDrawDate = retrieveNextDrawDate();
        if(dateTime.isAfter(nextDrawDate)) {
            return Collections.emptyList();
        }
            List<Ticket> allTicketByDrawDate = ticketRepository.findAllTicketsByDrawDate(dateTime);
            return allTicketByDrawDate.stream()
                    .map(TicketMapper::mapToDto)
                    .toList();
    }

    public LocalDateTime retrieveNextDrawDate(){
        return dateTimeDrawFacade.readNextDrawDate();
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