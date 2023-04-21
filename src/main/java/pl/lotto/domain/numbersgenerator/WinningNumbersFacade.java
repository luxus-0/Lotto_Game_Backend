package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.TicketIdGenerator;
import pl.lotto.domain.numberreceiver.TicketIdGeneratorImpl;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
public class WinningNumbersFacade {

    private static final String WINNING_NUMBERS_MESSAGE = "Winning numbers not found";
    private final DrawDateFacade drawDateFacade;
    private final RandomNumbersGenerable randomNumbersGenerable;
    private final WinningNumberValidator winningNumberValidator;
    private final WinningNumbersRepository winningNumbersRepository;

    public WinningNumbersDto generateWinningNumbers() {
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        RandomNumbersDto randomNumbers = randomNumbersGenerable.generateSixRandomNumbers();
        Set<Integer> winningNumbers = randomNumbers.randomNumbers();
        TicketIdGenerator ticketIdGenerator = new TicketIdGeneratorImpl();
        winningNumberValidator.validate(winningNumbers);
        WinningNumbers winningNumbersDocument = WinningNumbers.builder()
                .ticketId(ticketIdGenerator.generateTicketId())
                .winningNumbers(winningNumbers)
                .drawDate(nextDrawDate)
                .build();
        WinningNumbers saved = winningNumbersRepository.save(winningNumbersDocument);
        return WinningNumbersDto.builder()
                .ticketId(saved.ticketId())
                .winningNumbers(saved.winningNumbers())
                .drawDate(saved.drawDate())
                .build();
    }

    public WinningNumbersDto retrieveWinningNumbersByDate(LocalDateTime drawDate) {
        WinningNumbersDto winningNumbersDto = generateWinningNumbers();
        WinningNumbers winningNumbers = WinningNumbers.builder()
                .ticketId(winningNumbersDto.ticketId())
                .winningNumbers(winningNumbersDto.winningNumbers())
                .drawDate(drawDate)
                .build();
        WinningNumbers numbersByDate = winningNumbersRepository.findWinningNumbersByDrawDate(winningNumbers.drawDate()).orElseThrow(() -> new WinningNumbersNotFoundException(WINNING_NUMBERS_MESSAGE));
        return WinningNumbersDto.builder()
                .ticketId(numbersByDate.ticketId())
                .winningNumbers(numbersByDate.winningNumbers())
                .drawDate(numbersByDate.drawDate())
                .build();
    }

    public boolean areWinningNumbersGeneratedByDate() {
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        return winningNumbersRepository.existsByDrawDate(nextDrawDate);
    }
}
