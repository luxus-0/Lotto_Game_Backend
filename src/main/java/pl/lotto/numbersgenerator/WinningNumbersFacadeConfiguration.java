package pl.lotto.numbersgenerator;

import org.springframework.context.annotation.Configuration;
import pl.lotto.numberreceiver.NumbersValidator;
import pl.lotto.numberreceiver.Ticket;

@Configuration
public class WinningNumbersFacadeConfiguration {

    public WinningNumbersFacade createModuleForTests(WinningNumbersRepository winningNumbersRepository, Ticket ticket){
        NumbersGenerator numbersGenerator = new NumbersGenerator();
        NumbersValidator validator = new NumbersValidator();
        WinningNumbersValidator winningNumbersValidator = new WinningNumbersValidator(numbersGenerator, validator, ticket);
        return new WinningNumbersFacade(winningNumbersRepository, winningNumbersValidator);
    }
}
