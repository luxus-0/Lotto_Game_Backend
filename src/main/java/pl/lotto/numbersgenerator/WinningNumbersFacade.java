package pl.lotto.numbersgenerator;

import pl.lotto.numberreceiver.Ticket;
import pl.lotto.numbersgenerator.dto.WinningNumbersResultDto;

import static pl.lotto.numbersgenerator.WinningNumbersMessageProvider.FAILED;
import static pl.lotto.numbersgenerator.WinningNumbersMessageProvider.SUCCESS;

public class WinningNumbersFacade {
    private final WinningNumbersRepository winningNumbersRepository;
    private final WinningNumbersValidator winningNumbersValidator;

    public WinningNumbersFacade(WinningNumbersRepository winningNumbersRepository, WinningNumbersValidator winningNumbersValidator) {
        this.winningNumbersRepository = winningNumbersRepository;
        this.winningNumbersValidator = winningNumbersValidator;
    }

    public WinningNumbersResultDto checkWinnerNumbers(Ticket ticket) {
        if (winningNumbersValidator.isWinnerNumbers() != null) {
            winningNumbersRepository.save(ticket);
            return new WinningNumbersResultDto(ticket, SUCCESS);
        }
        return new WinningNumbersResultDto(ticket, FAILED);
    }
}
