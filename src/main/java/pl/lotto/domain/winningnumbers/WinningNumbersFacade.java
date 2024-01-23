package pl.lotto.domain.winningnumbers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.drawdate.exceptions.DrawDateNotFoundException;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.winningnumbers.dto.RandomNumbersResponseDto;
import pl.lotto.domain.winningnumbers.dto.WinningTicketResponseDto;
import pl.lotto.domain.winningnumbers.exceptions.WinningNumbersNotFoundException;
import pl.lotto.domain.randomnumbersgenerator.RandomNumbersGenerator;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static pl.lotto.domain.drawdate.DrawDateMessageProvider.DRAW_DATE_NOT_FOUND;
import static pl.lotto.domain.randomnumbersgenerator.RandomNumbersURL.*;
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.LOSE;

@AllArgsConstructor
@Log4j2
@Builder
public class WinningNumbersFacade {
    private final DrawDateFacade drawDateFacade;
    private final WinningNumbersRepository winningNumbersRepository;
    private final WinningNumbersValidator validator;
    private final NumberReceiverFacade numberReceiverFacade;
    private final RandomNumbersGenerator randomNumbersGenerator;
    private final WinningNumbersConfigurationProperties properties;
    private final WinningNumbersManager winningNumbersManager;

    public WinningTicketResponseDto generateWinningNumbers() {
        String ticketId = randomNumbersGenerator.generateTicketUUID();
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        RandomNumbersResponseDto randomNumbersResponse = randomNumbersGenerator.generateRandomNumbers(COUNT_RANDOM_NUMBERS, LOWER_BAND_RANDOM_NUMBERS, UPPER_BAND_RANDOM_NUMBERS);
        Set<Integer> randomNumbers = randomNumbersResponse.randomNumbers();
        Set<Integer> inputNumbers = numberReceiverFacade.retrieveInputNumbersByDrawDate(nextDrawDate);
        Set<Integer> winningNumbers = winningNumbersManager.retrieveWinningNumbers(randomNumbers, inputNumbers);
        boolean validate = validator.validate(winningNumbers);
        if (validate) {
            WinningNumbers winningTicket = new WinningNumbers(ticketId, winningNumbers, nextDrawDate);
            WinningNumbers savedWinningNumbers = winningNumbersRepository.save(winningTicket);

            return WinningTicketResponseDto.builder()
                    .ticketUUID(savedWinningNumbers.ticketUUID())
                    .winningNumbers(savedWinningNumbers.winningNumbers())
                    .drawDate(savedWinningNumbers.drawDate())
                    .build();
        }
        return WinningTicketResponseDto.builder()
                .winningNumbers(Set.of())
                .message(LOSE)
                .build();
    }

    public WinningTicketResponseDto retrieveWinningNumbersByDate(LocalDateTime drawDate) {
        return Optional.ofNullable(drawDate)
                .map(date -> winningNumbersRepository.findWinningNumbersByDrawDate(date).stream()
                        .map(winningNumbers -> WinningTicketResponseDto.builder()
                        .ticketUUID(winningNumbers.ticketUUID())
                        .drawDate(winningNumbers.drawDate())
                        .winningNumbers(winningNumbers.winningNumbers())
                        .build())
                .findAny()
                .orElseThrow(WinningNumbersNotFoundException::new))
                .orElseThrow(() -> new DrawDateNotFoundException(DRAW_DATE_NOT_FOUND));
    }

    public boolean areWinningNumbersGeneratedByDate() {
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        if(nextDrawDate.isAfter(LocalDateTime.now(Clock.systemDefaultZone()))) {
            return winningNumbersRepository.existsByDrawDate(nextDrawDate);
        }
        return false;
    }
}
