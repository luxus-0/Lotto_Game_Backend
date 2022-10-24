package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.NumbersResultMessageDto;
import pl.lotto.numberreceiver.dto.TicketMessageDto;
import pl.lotto.numberreceiver.enums.ValidateMessage;
import pl.lotto.numberreceiver.exception.DuplicateNumbersNotFoundException;
import pl.lotto.numberreceiver.exception.NumbersNotFoundException;
import pl.lotto.numberreceiver.exception.RangeNumbersException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static pl.lotto.numberreceiver.DrawDateGenerator.generateDrawDate;
import static pl.lotto.numberreceiver.GeneratedTicketMessageProvider.generated_ticket_message_failed;
import static pl.lotto.numberreceiver.GeneratedTicketMessageProvider.generated_ticket_message_ok;
import static pl.lotto.numberreceiver.NumbersDuplicationCounter.printDuplicatedNumbersInfo;
import static pl.lotto.numberreceiver.NumbersMessageProvider.*;
import static pl.lotto.numberreceiver.TicketIdGenerator.generateHash;
import static pl.lotto.numberreceiver.enums.ValidateMessage.*;

public class NumberReceiverFacade {

    private final NumbersValidator numberValidator;
    private final TicketRepository ticketRepository;

    public NumberReceiverFacade(NumbersValidator numberValidator, TicketRepository ticketRepository) {
        this.numberValidator = numberValidator;
        this.ticketRepository = ticketRepository;
    }

    public NumbersResultMessageDto isLessThanSixNumbers(Set<Integer> inputNumbers) {
        if (numberValidator.checkLessThanSixNumbers(inputNumbers)) {
            NumbersResultMessageDto notCorrectResults = new NumbersResultMessageDto(inputNumbers, FAILED_MESSAGE);
            return Optional.of(notCorrectResults).orElse(new NumbersResultMessageDto(inputNumbers, SUCCESS_MESSAGE));
        }
        throw new NumbersNotFoundException();
    }

    public NumbersResultMessageDto isEqualsSixNumbers(Set<Integer> inputNumbers) {
        if (numberValidator.checkEqualsSixNumbers(inputNumbers)) {
            NumbersResultMessageDto result = new NumbersResultMessageDto(inputNumbers, CORRECT_SIZE_NUMBERS.name());
            return new NumbersResultMessageDto(inputNumbers, result.message());
        }
        return new NumbersResultMessageDto(inputNumbers, INVALID_MESSAGE);
    }

    public NumbersResultMessageDto isMoreThanSixNumbers(Set<Integer> inputNumbers) {
        if (numberValidator.checkMoreThanSixNumbers(inputNumbers)) {
            return Optional.of(new NumbersResultMessageDto(inputNumbers, NOT_CORRECT_SIZE_NUMBERS.name()))
                    .orElseGet(() -> new NumbersResultMessageDto(inputNumbers, CORRECT_SIZE_NUMBERS.name()));
        }
        return new NumbersResultMessageDto(null, UNKNOWN_SIZE_NUMBERS.name());

    }

    public ValidateMessage isDuplicateNumbers(List<Integer> numbersCheck) {
        NumbersDuplicationChecker numbersFinder = new NumbersDuplicationChecker();
        if (numbersFinder.checkIdenticalNumbers(numbersCheck)) {
            return printDuplicatedNumbersInfo(numbersCheck);
        }
        throw new DuplicateNumbersNotFoundException();
    }

    public ValidateMessage isNumbersNotInRange(Set<Integer> inputNumbers) {
        NumbersRangeChecker numbers = new NumbersRangeChecker();
        if (numbers.checkNumbersInRange(inputNumbers)) {
            return IN_RANGE_NUMBERS;
        }
        throw new RangeNumbersException();
    }

    public TicketMessageDto isGeneratedTicket(Set<Integer> inputNumbers) {
        if (inputNumbers.isEmpty()) {
            numbers_not_found();
        }
        if (numberValidator.checkEqualsSixNumbers(inputNumbers)) {
            String hash = generateHash();
            LocalDateTime date = generateDrawDate();
            Ticket generatedTicket = Ticket.builder()
                    .hash(hash)
                    .numbers(new TreeSet<>(inputNumbers))
                    .drawDate(date)
                    .build();
            ticketRepository.save(generatedTicket);
            Optional<Ticket> addedTicket = ticketRepository.findByHash(hash);
            if (addedTicket.isPresent()) {
                return new TicketMessageDto(generatedTicket, generated_ticket_message_ok());
            }
        }
        return new TicketMessageDto(null, generated_ticket_message_failed());
    }
}
