package pl.lotto.numbersgenerator;

import pl.lotto.numberreceiver.NumbersValidator;
import pl.lotto.numberreceiver.Ticket;
import pl.lotto.numbersgenerator.dto.WinningNumbersResultDto;

import java.util.Optional;
import java.util.Set;

import static pl.lotto.numbersgenerator.WinningNumbersMessageProvider.FAILED;
import static pl.lotto.numbersgenerator.WinningNumbersMessageProvider.SUCCESS;

class WinningNumbersValidator {

    private final NumbersGenerator numbersGenerator;
    private final NumbersValidator numbersValidator;
    private final Ticket ticket;

    WinningNumbersValidator(NumbersGenerator numbersGenerator, NumbersValidator numbersValidator, Ticket ticket) {
        this.numbersGenerator = numbersGenerator;
        this.numbersValidator = numbersValidator;
        this.ticket = ticket;
    }

    public WinningNumbersResultDto isWinnerNumbers() {
        Set<Integer> inputNumbers = ticket.numbersUser();
        Integer numberFromUser = inputNumbers.stream().findAny().orElse(0);
        Set<Integer> randomNumbers = numbersGenerator.generateNumbers();
        if(randomNumbers.contains(numberFromUser) && numbersValidator.validate(inputNumbers)){
            return new WinningNumbersResultDto(ticket, SUCCESS);
        }
       return Optional.of(new WinningNumbersResultDto(ticket, FAILED))
               .orElseThrow();
    }
}
